package com.kodex.guide.ui.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream

object ImageUtils {

    fun imageToBase64(uri: Uri, contentResolver: ContentResolver): String{
        val bytes = uriToByteArray(uri,contentResolver)
        val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)

        Log.d("MyLog", "base64Image size: , ${base64Image.toByteArray(Charsets.UTF_8).size}")
        return base64Image
    } 

    fun uriToByteArray(uri: Uri, contentResolver: ContentResolver): ByteArray{
        val inputStream = contentResolver.openInputStream(uri)
        val bm = BitmapFactory.decodeStream(inputStream)
        val resizedBitMap = resizedBitMapImage(bm, 300)
        val stream = ByteArrayOutputStream()
        if (Build.VERSION.SDK_INT >= 30){
            resizedBitMap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 70, stream)
        }else{
            resizedBitMap.compress(Bitmap.CompressFormat.WEBP, 70, stream)
    }
    return stream.toByteArray()
}


    fun resizedBitMapImage(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        if (width <= maxSize && height <= maxSize) return bitmap
        val imageRatio = width.toFloat() / height.toFloat()

        if (imageRatio > 1) {
            width = maxSize
            height = (width / imageRatio).toInt()
        } else {
            height = maxSize
            width = (height * imageRatio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
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
}
