package com.kodex.guide.utils.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.model.RatingData
import com.kodex.guide.domain.model.AddressData
import com.kodex.guide.domain.model.PersonalData
import com.kodex.guide.domain.model.UserSettingsData
import com.kodex.guide.utils.Categories.ALL
import com.kodex.guide.utils.FirebaseConst.RATING_DATA
import com.kodex.guide.utils.FirebaseConst.MODERATION
import com.kodex.guide.utils.FirebaseConst.RATING
import com.kodex.guide.utils.FirebaseConst.POSTS
import com.kodex.guide.utils.FirebaseConst.ADDRESS_DATA
import com.kodex.guide.utils.FirebaseConst.DATA
import com.kodex.guide.utils.FirebaseConst.PERSONAL_DATA
import com.kodex.guide.utils.FirebaseConst.USER_DATA
import com.kodex.guide.utils.FirebaseConst.USER_SETTINGS
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

const val IS_BASE_64 = true

@Singleton
class FireStoreManagerPaging @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    // private val contentResolver: ContentResolver
    // private val storage: FirebaseStorage,
) {
    var categoryIndex: Int = ALL
    var searchText = ""
    var minPrice = 0
    var maxPrice = 5000
    var isTitleFilter = false
    var isPriceFilter = false

   /* suspend fun nextPage(
        pageSize: Long,
        currentKey: DocumentSnapshot?,
    ): Pair<QuerySnapshot, List<Book>> {
        var query: Query = db.collection(POSTS).limit(pageSize)
        //  .orderBy(FirebaseConst.TITLE)
        // val keysFavesList = getIdsFavesList()

        query = when (categoryIndex) {
            ALL -> query
            //Categories.FAVORITES -> query.whereIn(FieldPath.of(KEY), keysFavesList)
            else -> query.whereEqualTo(CATEGORY_INDEX, categoryIndex)
        }

        if (searchText.isNotEmpty()) {
            query = query.whereGreaterThanOrEqualTo(SEARCH_TITLE, searchText.lowercase())
                .whereLessThan(SEARCH_TITLE, "${searchText.lowercase()}\uF7FF")
        }

        if (!isPriceFilter) {
            query = query.whereGreaterThanOrEqualTo(FirebaseConst.PRICE, minPrice)
                .whereLessThanOrEqualTo(FirebaseConst.PRICE, maxPrice)
        }
        if (currentKey != null) {
            query = query.startAfter(currentKey)
        }
        val querySnapshot = query.get().await()
        val books = querySnapshot.toObjects(Book::class.java)
        *//*  val updatedBooks = books.map {
            if (keysFavesList.contains(it.key)) {
                it.copy(isFavorite = true)
            } else {
                it
            }
        }
        return Pair(querySnapshot, updatedBooks)
    }
    *//*

    }*/
/*
        fun getFavesCategoryReference(): CollectionReference {
            return db.collection(USERS)
                .document(auth.uid ?: "")
                .collection(FirebaseConst.FAVORITES)
        }*/


        fun saveBookToFireStore(
            book: Book,
            onSaved: () -> Unit,
            onError: (String) -> Unit,
        ) {
            val db = db.collection(POSTS)
            val key = if (book.key.isEmpty()) db.document().id else book.key
            db.document(key)
                .set(
                    book.copy(key = key)
                ).addOnSuccessListener {
                    onSaved()
                }
                .addOnFailureListener { exception ->
                    onError(exception.message ?: "Error saved book")
                }
            onError
        }

        fun uploadImageToFirestore(
            oldImageUrl: String,
            uri: Uri?,
            book: Book,
            onSaved: () -> Unit,
            onError: (String) -> Unit,
        ) {
            val timeStamp = System.currentTimeMillis()
            val storageRef = if (oldImageUrl.isEmpty()) {
                // storage!!.reference
                //   .child("spark_posts")
                //   .child("image_$timeStamp.jpg")
            } else {
                // storage?.getReferenceFromUrl(oldImageUrl)
            }
            if (uri == null) {
                saveBookToFireStore(
                    book.copy(imageUrl = oldImageUrl),
                    onSaved = {
                        onSaved()
                    },
                    onError = { massage ->
                        onError(massage)
                    }
                )
                return
            }
        }
    suspend fun getCommentsToModerate(): List<RatingData> {
        val querySnapshot = db.collection(MODERATION)
            .get().await()
        val commentsList = querySnapshot.toObjects(RatingData::class.java)
        return commentsList
    }
    suspend fun insertModerationRating(ratingData: RatingData) {
        if (auth.uid == null) return
        db.collection(RATING)
            .document(ratingData.bookId)
            .collection(RATING_DATA)
            .document(ratingData.uid)
            .set(ratingData)

        val book: Book = db.collection(POSTS)
            .document(ratingData.bookId)
            .get().await().toObject(Book::class.java) ?: return
        val ratingsList = book.ratingsList.toMutableList()
        if (ratingData.lastRating == 0) {
            ratingsList.add(ratingData.rating)
        } else {
            val index = ratingsList.indexOf(ratingData.lastRating)
            ratingsList[index] = ratingData.rating
        }
        db.collection(POSTS)
            .document(ratingData.bookId)
            .update("ratingsList", ratingsList)
    }

    fun updateLastVisit() {
        if (auth.uid == null) return
        db.collection(USER_DATA)
            .document(auth.uid!!)
            .collection(PERSONAL_DATA)
            .document(DATA)
            .update("lastVisit", System.currentTimeMillis())
    }

    fun insertUserRating(ratingData: RatingData, bookId: String) {
        if (auth.uid == null) return
        db.collection(MODERATION)
            .document(auth.uid!!)
            .set(
                ratingData.copy(
                    name = auth.currentUser?.email ?: "Unknown",
                    uid = auth.uid!!,
                    bookId = bookId
                )
            )
    }




    suspend fun deleteComment(uid: String) {
        db.collection(MODERATION)
            .document(uid)
            .delete().await()
    }

    suspend fun getBookComments(bookId: String): List<RatingData> {
        val querySnapshot = db.collection(RATING)
            .document(bookId)
            .collection(RATING_DATA)
            .get().await()
        return querySnapshot.toObjects(RatingData::class.java)
    }

    suspend fun getUserRating(bookId: String): RatingData? {
        if (auth.uid == null) return null
        val querySnapshot = db.collection(RATING)
            .document(bookId)
            .collection(RATING_DATA)
            .document(auth.uid!!)
            .get().await()
        return querySnapshot.toObject(RatingData::class.java)
    }

    fun insertPersonalData(personalData: PersonalData, onDataSaved: () -> Unit = {}) {
        if (auth.uid == null) return
        db.collection(USER_DATA)
            .document(auth.uid!!)
            .collection(PERSONAL_DATA)
            .document(DATA)
            .set(personalData).addOnSuccessListener {
                onDataSaved()
            }
    }

    fun insertAddressData(addressData: AddressData) {
        if (auth.uid == null) return
        db.collection(USER_DATA)
            .document(auth.uid!!)
            .collection(ADDRESS_DATA)
            .document(DATA)
            .set(addressData)

    }

    fun insertUserSettingsData(userSettingsData: UserSettingsData) {
        if (auth.uid == null) return
        db.collection(USER_DATA)
            .document(auth.uid!!)
            .collection(USER_SETTINGS)
            .document(DATA)
            .set(userSettingsData)

    }

    suspend fun getSettings(
        onSettingsLoaded: (PersonalData, AddressData, UserSettingsData) -> Unit,
    ) {
        if (auth.uid == null) return
        val querySnapshotPersonal = db.collection(USER_DATA)
            .document(auth.uid!!)
            .collection(PERSONAL_DATA)
            .document(DATA)
            .get().await()
        val personalData =
            querySnapshotPersonal.toObject(PersonalData::class.java) ?: PersonalData()

        val querySnapshotAddress = db.collection(USER_DATA)
            .document(auth.uid!!)
            .collection(ADDRESS_DATA)
            .document(DATA)
            .get().await()
        val addressData =
            querySnapshotAddress.toObject(AddressData::class.java) ?: AddressData()

        val querySnapshotSettings = db.collection(USER_DATA)
            .document(auth.uid!!)
            .collection(USER_SETTINGS)
            .document(DATA)
            .get().await()
        val userSettingsData =
            querySnapshotSettings.toObject(UserSettingsData::class.java) ?: UserSettingsData()

        onSettingsLoaded(personalData, addressData, userSettingsData)

    }

}


/*
//        val imageBytes = ImageUtils.uriToBiteArray(uri, contentResolver)
//        val uploadTask = storageRef?.putBytes(imageBytes)
//        uploadTask?.addOnSuccessListener{
//            storageRef.downloadUrl.addOnSuccessListener{url ->
//                saveBookToFireStore(
//                    book.copy(imageUrl = url.toString()),
//                    onSaved = {
//                        onSaved()
//                    },
//                    onError = {massage->
//                        onError(massage)
//                    }
//                )
//            }
//        }
        }

        fun saveBookImage(
            oldImageUrl: String,
            uri: Uri?,
            book: Book,
            onSaved: () -> Unit,
            onError: (String) -> Unit,
        ) {
            if (IS_BASE_64) {
                *//*  saveBookToFireStore(
                book,
                onSaved = {
                    onSaved()
                },
                onError = {
                    onError("Error save Image1 ")
                },
            )*//*
            } else {
                uploadImageToFirestore(
                    oldImageUrl = oldImageUrl,
                    uri = uri,
                    book = book,
                    onSaved = {
                        onSaved()
                    },
                    onError = {
                        onError("Error save Image2")
                    }
                )
            }
        }

        */





