package com.kodex.guide.ui.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.addscreen.data.Book
import com.kodex.guide.ui.utils.Categories
import com.kodex.guide.ui.utils.toBitmap


@Composable
fun BookListItemUi(
    titleIndex: Int,
    showEditButton: Boolean = true,
    book: Book = Book(),
    onEditClick: (Book) -> Unit = {},
    onDeleteClick: (Book) -> Unit = {},
    onFavClick: () -> Unit = {},
    onBookClick: (Book) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onBookClick(book)
            }
    ) {


        AsyncImage(
            model = book.imageUrl.toBitmap() ?: book.imageUrl,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //            verticalAlignment = Alignment.CenterVertically
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                text = book.title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Icon(
                Icons.Default.Star,
                contentDescription = ""
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringArrayResource(id = R.array.category_arrays)[book.categoryIndex],
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = book.description,
            color = Color.Gray,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                text = book.price.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            if (showEditButton) IconButton(onClick = {
                onEditClick(book)

            }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = ""
                )
            }


            if (showEditButton) IconButton(onClick = {
                onDeleteClick(book)
            }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = ""
                )
            }



            IconButton(onClick = {
                onFavClick()
            }) {
                Icon(
                    if (book.isFaves) {
                        Icons.Default.Favorite
                    } else
                        Icons.Default.FavoriteBorder,
                    contentDescription = ""
                )
            }
        }
    }
}
