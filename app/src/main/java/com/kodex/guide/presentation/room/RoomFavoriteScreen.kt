package com.kodex.guide.presentation.room


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.kodex.bookmarketcompose.R
import com.kodex.guide.domain.model.Book
import com.kodex.guide.presentation.home.ProgressBar
import kotlin.collections.emptyList


@Composable
fun RoomFavoriteScreen(
    viewModel: RoomFavoriteViewModel = hiltViewModel(),
    onTrackClick: (Book) -> Unit = {},



   // navData: NavRoutes.TrackerDataObject,
   // onBookEditClick: (Book) -> Unit,
    onBookClick: (Book) -> Unit,
   // onAdminClick: () -> Unit,
    //onLoginClick: () -> Unit,
    //onAddBookClick: () -> Unit,
   // onSettingsClick: () -> Unit,
) {
    val book = viewModel.postList.collectAsState(initial = emptyList())
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Log.d("MyLog", "${book}")


    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){
        if (book.value.isEmpty()){
            ProgressBar()
           /* LaunchedEffect(Unit) {
                delay(1000)
            }*/
            // Text(text = stringResource(R.string.no_tracks))

        }else{
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding( 2.dp))
            {
                items(book.value) { trackItem ->
                    RoomItemUi(
                        trackItem,
                        onDeleteClick = {
                            showDialog.value = true
                            viewModel.postToDelete = trackItem

                        },
                        onItemClick = {
                            onTrackClick(trackItem)
                        }
                    )

                    Spacer(Modifier.padding(5.dp))
                }
            }
        }
    }

    RoomDialog(
        title = stringResource(R.string.delete_dialog_massage),
        showDialog = showDialog.value,
        dialogType = DialogType.DELETE,
        onDismiss = { showDialog.value = false },
        onConfirm = {
            viewModel.deletePost()
            showDialog.value = false
        }
    )
}

