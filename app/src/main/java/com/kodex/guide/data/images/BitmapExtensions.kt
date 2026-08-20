package com.kodex.guide.data.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlin.io.encoding.Base64

    fun String.toBitmap(): Bitmap?{
        return try {
            val base64Image = Base64.decode(this)
            BitmapFactory.decodeByteArray(
                base64Image,0,
                base64Image.size
            )
        }catch (e: IllegalArgumentException){
            Log.d("MyLog2", "toBitmap size: , ${this.toByteArray(Charsets.UTF_8).size}")
            null
        }
    }
