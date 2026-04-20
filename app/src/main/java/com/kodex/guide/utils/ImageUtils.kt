package com.kodex.guide.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.scale
import com.kodex.guide.domain.model.UserSettingsData
import java.io.ByteArrayOutputStream

object ImageUtils {
     val sizeList = listOf(
        100,
        200,
        300,
        600
    )
    val qualityList = listOf(
        10,
        50,
        100,

    )
    @RequiresApi(Build.VERSION_CODES.R)
    val formatListAboveV30 = listOf(
        Bitmap.CompressFormat.WEBP_LOSSY,
        Bitmap.CompressFormat.WEBP_LOSSLESS,
        Bitmap.CompressFormat.JPEG,
        Bitmap.CompressFormat.PNG
    )
    val formatList = listOf(
        Bitmap.CompressFormat.WEBP,
        Bitmap.CompressFormat.WEBP,
        Bitmap.CompressFormat.JPEG,
        Bitmap.CompressFormat.PNG
    )
    fun imageToBase64(
        uri: Uri,
        contentResolver: ContentResolver,
        userSettingsData: UserSettingsData
    ): String{

        val bytes = uriToBiteArray(uri, contentResolver, userSettingsData)
        val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

        val inputStream = contentResolver.openInputStream(uri)
        val bm = BitmapFactory.decodeStream(inputStream)
        val resizedBitMap = resizeBitMapImage(bm, sizeList[userSettingsData.size])
        val stream = ByteArrayOutputStream()

        /*   if (Build.VERSION.SDK_INT >= 30){
               resizedBitMap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 30, stream)
           }else
               resizedBitMap.compress(Bitmap.CompressFormat.WEBP, 30, stream)*/
        Log.d("MyLog1", "base64Image size: , ${bytes.size}")
        Log.d("MyLog2", "base64Image size: , ${base64Image.toByteArray(Charsets.UTF_8).size}")
        return base64Image
    }
    fun uriToBiteArray(
        uri: Uri,
        contentResolver: ContentResolver,
        userSettingsData: UserSettingsData
    ): ByteArray{
        val inputStream = contentResolver.openInputStream(uri)
        val bm = BitmapFactory.decodeStream(inputStream)
        val resizedBitMap = resizeBitMapImage(bm, sizeList[userSettingsData.size])
        val stream = ByteArrayOutputStream()
           if (Build.VERSION.SDK_INT >= 30){
                resizedBitMap.compress(formatListAboveV30[userSettingsData.imageFormat], qualityList[userSettingsData.quality] , stream)
            }else {
                    resizedBitMap.compress(formatList[userSettingsData.imageFormat], qualityList[userSettingsData.quality] , stream)
            }
        resizedBitMap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
            // Log.d("MyLog3", "base64Image size: , ${bytes.size}")
            // Log.d("MyLog4", "base64Image size: , ${base64Image.toByteArray(Charsets.UTF_8).size}")
    }
    //изменяем размер картинки
    private  fun resizeBitMapImage(bitmap: Bitmap, maxSize: Int): Bitmap{
        var width = bitmap.width
        var height = bitmap.height
        if (width <= maxSize && height <= maxSize) return bitmap

        val imageRatio = width.toFloat() / height.toFloat()
        if (imageRatio > 1){
            width = maxSize
            height = (width / imageRatio).toInt()
        }else{
            height = maxSize
            width = (height * imageRatio).toInt()
        }
        return bitmap.scale(width, height, false)
    }
}

fun String.toBitmap(): Bitmap? {
    return try {
        val base64Image = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(
            base64Image, 0,
            base64Image.size

        )

    } catch (e: IllegalArgumentException) {
        null


    }
    Log.d("MyLog3", "toBitmap size: , ${this.toByteArray(Charsets.UTF_8).size}")

}