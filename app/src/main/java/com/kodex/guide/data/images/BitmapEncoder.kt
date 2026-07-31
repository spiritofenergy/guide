package com.kodex.guide.data.images

 import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
 import com.kodex.guide.data.model.UserSettingsDataDTO
 import com.kodex.guide.domain.model.UserSettingsData
 import javax.inject.Inject

class BitmapEncoder @Inject constructor(
    private  val bitmapResizer: BitmapResizer,
    private val bitmapCompressor: BitmapCompressor,
    private val imageLoader: ImageLoader,
    //private val settingsData: UserSettingsData
) {
    fun uriToByteArray(uri: Uri): ByteArray{
        val bitMap = imageLoader.loadImage(uri)
        val resizedBitMap = bitmapResizer.resize(bitMap, 200)
        return bitmapCompressor.compress(resizedBitMap)
    }
    fun imageToBase64(uri: Uri?): String{
        if (uri == null) return ""
        val bytes = uriToByteArray(uri)
        val base64Image = Base64.encodeToString(bytes, Base64.DEFAULT)
        Log.d("MyLog2", "base64Image size toByteArray: , ${base64Image.toByteArray(Charsets.UTF_8).size}")
        Log.d("MyLog1", "base64Image size: , ${bytes.size}")
        return base64Image
    }
}