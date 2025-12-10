package com.kodex.guide.ui.detailScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Preview(showBackground = true)
 @Composable
fun DetailScreen(
    navObject: DetailNavObject = DetailNavObject()
) {
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ){
        var bitmap: Bitmap? = null
        try {
            val base64Image = Base64.decode(navObject.imageUrl, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(
                base64Image,0,
                base64Image.size
            )
        }catch (e:IllegalArgumentException){

        }
        Column(modifier = Modifier.fillMaxWidth()) {
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)){
                AsyncImage(
                    model = bitmap,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .height(190.dp)
                        .background(Color.LightGray
                        ),
                    contentScale = ContentScale.FillHeight
                )
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally)
                {

                    Text(text = "Категория:",
                        color = Color.Gray)
                    Text(
                        text = navObject.categoryIndex.toString(),
                        fontWeight = FontWeight.Bold)
                    Text(text = "Автор:",
                        color = Color.Gray)
                    Text(
                        text = "Роман:",
                        fontWeight = FontWeight.Bold)
                    Text(text = "Дата печати:",
                        color = Color.Gray)
                    Text(text = "14.12.1985",
                        fontWeight = FontWeight.Bold)
                    Text(text = "Оценка:",
                        color = Color.Gray)
                    Text(text = "4.5",
                        fontWeight = FontWeight.Bold)

                }
            }
            Text(
                text = navObject.title,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Spacer(modifier = Modifier.fillMaxWidth().padding(10.dp))
            Text(
                text = navObject.description,
               // text = "«Властелин колец» является одним из самых крупных проектов в истории кино. Его реализация заняла восемь лет; все три фильма были сняты одновременно в Новой Зеландии, родной стране Питера Джексона. У каждого из фильмов трилогии есть специальная расширенная версия, выпущенная на DVD спустя год после выхода соответствующей театральной версии. Фильмы следуют за основной сюжетной линией книги, но опускают некоторые существенные элементы повествования, включают дополнения и отклонения от исходного материала.",
                fontSize = 16.sp
            )
        }

        Column (modifier = Modifier.fillMaxWidth() ){
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {

                }) {
                Text(text = "Добавить в корзину")
            }
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {

                }) {
                Text(text = "${navObject.price} Bay Now")
            }
        }



    }
}