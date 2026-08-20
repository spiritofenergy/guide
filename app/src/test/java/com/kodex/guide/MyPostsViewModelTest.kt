package com.kodex.guide

 import com.kodex.guide.data.images.BitmapEncoder
import com.kodex.guide.domain.model.Book
import com.kodex.guide.domain.tarif.AuthStateProvider
import com.kodex.guide.domain.usecase.DeleteMyPostUseCase
import com.kodex.guide.domain.usecase.GetMyPostUseCase
import com.kodex.guide.domain.usecase.ObserveMyPostsUseCase
import com.kodex.guide.domain.usecase.SaveDraftUseCase
import com.kodex.guide.domain.usecase.UploadMyPostUseCase
import com.kodex.guide.presentation.myPosts.MyPostsEvent
import com.kodex.guide.presentation.myPosts.MyPostsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk


 import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import com.google.firebase.auth.FirebaseUser
 import com.kodex.guide.domain.user.AuthUser

@OptIn(ExperimentalCoroutinesApi::class)
class MyPostsViewModelTest {

    private val observe = mockk<ObserveMyPostsUseCase> {
        every { invoke(uid = any()) } returns flowOf(emptyList())

    }
    private val getPost = mockk<GetMyPostUseCase>()
    private val save = mockk<SaveDraftUseCase>()
    private val upload = mockk<UploadMyPostUseCase>()
    private val delete = mockk<DeleteMyPostUseCase>()
    private val encoder = mockk<BitmapEncoder>()
    private val authProvider = mockk<AuthStateProvider> {
        every { currentUser() } returns null
    }

    private lateinit var viewModel: MyPostsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = MyPostsViewModel(
            observe, getPost, save, upload, delete, encoder, authProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `save шлет тост и публикует при publish = true`(): Unit = runTest {
        val book = Book(key = "k1", title = "Т")
        coEvery { save(book) } returns Result.success(book)
        coEvery { upload(book) } returns Result.success(Unit)

        val events = mutableListOf<MyPostsEvent>()
        val job = launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.events.collect { events.add(it) }
        }

        viewModel.save(book, publish = true)
        advanceUntilIdle()
        job.cancel()

        Assert.assertEquals(
            listOf(
                MyPostsEvent.Toast("Сохранено на устройстве"),
                MyPostsEvent.Toast("Опубликовано")
            ),
            events
        )
        coVerify(exactly = 1) { upload(book) }
    }

    @Test
    fun `ошибка сохранения превращается в тост`() = runTest {
        val book = Book(key = "k2")
        coEvery { save(book) } returns Result.failure(Throwable("диск полон"))

        val events = mutableListOf<MyPostsEvent>()
        val job = launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.events.collect { events.add(it) }
        }

        viewModel.save(book)
        advanceUntilIdle()
        job.cancel()

        Assert.assertEquals(listOf(MyPostsEvent.Toast("диск полон")), events)
        coVerify(exactly = 0) { upload(any()) }
    }

    @Test
    fun `delete шлет тост Удалено`() = runTest {
        val book = Book(key = "k3")
        coEvery { delete(book) } returns Result.success(Unit)

        val events = mutableListOf<MyPostsEvent>()
        val job = launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.events.collect { events.add(it) }
        }

        viewModel.delete(book)
        advanceUntilIdle()
        job.cancel()

        Assert.assertEquals(listOf(MyPostsEvent.Toast("Удалено")), events)
    }
    @Test
    fun `init загружает свои посты если пользователь залогинен`() = runTest {
        // ✅ залогиненный пользователь
        val user = mockk<AuthUser> { every { uid } returns "user1" }
        val authWithUser = mockk<AuthStateProvider> {
            every { currentUser() } returns user
        }
        val book = Book(key = "p1", title = "Мой пост")
        val observeMock = mockk<ObserveMyPostsUseCase> {
            every { invoke(uid = "user1") } returns flowOf(listOf(book))
        }

        val vm = MyPostsViewModel(
            observeMock, getPost, save, upload, delete, encoder, authWithUser
        )
        advanceUntilIdle()

        // ✅ посты из flow попали в состояние
        Assert.assertEquals(listOf(book), vm.myPosts.value)
        // ✅ UseCase вызван ровно один раз с нужным uid
        coVerify(exactly = 1) { observeMock(uid = "user1") }
    }
}