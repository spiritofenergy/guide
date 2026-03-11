package com.kodex.guide.ui.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun imageToBase64(uri: Uri, contentResolver: ContentResolver): String{

        val bytes = uriToBiteArray(uri, contentResolver)
        val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
        /*   if (Build.VERSION.SDK_INT >= 30){
               resizedBitMap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 30, stream)
           }else
               resizedBitMap.compress(Bitmap.CompressFormat.WEBP, 30, stream)*/
        Log.d("MyLog1", "base64Image size: , ${bytes.size}")
        Log.d("MyLog2", "base64Image size: , ${base64Image.toByteArray(Charsets.UTF_8).size}")
        return base64Image
    }
    fun uriToBiteArray(uri: Uri, contentResolver: ContentResolver): ByteArray{
        val inputStream = contentResolver.openInputStream(uri)
        val bm = BitmapFactory.decodeStream(inputStream)
        val resizedBitMap = resizeBitMapImage(bm, 300)
        val stream = ByteArrayOutputStream()
           if (Build.VERSION.SDK_INT >= 30){
                resizedBitMap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 70, stream)
            }else {
                resizedBitMap.compress(Bitmap.CompressFormat.WEBP, 70, stream)
            }
        resizedBitMap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
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