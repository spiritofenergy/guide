package com.kodex.guide.ui.mainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FireTruck
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kodex.guide.domain.model.Book
import com.kodex.guide.ui.utils.toBitmap
 import com.kodex.bookmarketcompose.R
import com.kodex.guide.ui.theme.GreenSea
import com.kodex.guide.ui.theme.Orange


@SuppressLint("DefaultLocale")
@Composable
fun BookListItemUi(
    titleIndex: Int,
    showEditButton: Boolean = true,
    book: Book = Book(),
    onEditClick: (Book) -> Unit = {},
    onDeleteClick: (Book) -> Unit = {},
    onFavesClick: () -> Unit = {},
    onBookClick: (Book) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .clickable {
                onBookClick(book)
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            // 1. Фоновое изображение
            AsyncImage(
                model = book.imageUrl.toBitmap(),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(10.dp))
            )

            // 2. Категория в левом верхнем углу
            Text(
                " " + stringArrayResource(id = R.array.category_array)[book.categoryIndex]+ " ",
               // " " + stringArrayResource(R.array.category_array)[book.categoryIndex] + " ",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 10.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            )

            // 3. MoreVert в правом верхнем углу
            /*IconButton(
                onClick = { },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "",
                    modifier = Modifier
                        .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
                        .padding(8.dp),
                    tint = Color.White
                )
            }*/

            // 4. Bookmark в правом нижнем углу
            IconButton(
                onClick = { onFavesClick() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            ) {
                Icon(
                    if (book.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "",
                    modifier = Modifier
                        .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(15.dp))
                        .padding(8.dp),
                    tint = if (book.isFavorite) colorResource(R.color.orang) else Color.White
                )
            }

            // 5. Рейтинг в левом нижнем углу
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Gray.copy(alpha = 0.5f))
                    .padding(horizontal = 10.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                 if (book.ratingsList.isNotEmpty()) {
                Text(
                    text = String.format("%.1f", book.ratingsList.average()),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                  } else {
                Text(text = "0.0")
            }
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    modifier = Modifier.size(22.dp),
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = Orange
                )
        }
    }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                text = book.title,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = book.description,
            color = Color.Gray,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            fontSize = 16.sp
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.height(10.dp))
            if (!showEditButton)
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F),
                    text = book.price.toString() + " p",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    fontSize = 18.sp
                )
            Icon(
                Icons.Default.DeliveryDining,
                contentDescription = "Location",
                modifier = Modifier.size(20.dp),
                tint = GreenSea

            )
            Spacer(modifier = Modifier.width(5.dp))
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "Location",
                modifier = Modifier.size(16.dp),
                tint = Orange

            )

            if (!showEditButton) {
                Text(
                    modifier = Modifier

                        .padding(10.dp),
                    text = book.village,
                    color = Color.Black,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                )
            }
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
        }
    }
}
@Composable
@Preview(showBackground = true)
fun BookListItemUiPreview() {
    BookListItemUi(
        titleIndex = 0,
    )
}