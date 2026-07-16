package com.kodex.guide.data.images

import android.R.attr.bitmap
import android.graphics.Bitmap
import android.os.Build
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitmapCompressor @Inject constructor() {
    fun compress(bitmap: Bitmap,
                 quality: Int = 50,
                // userSettingsData: UserSettingsData,
                 imageFormat: Bitmap.CompressFormat = defaultImageFormat()): ByteArray{
        val stream = ByteArrayOutputStream()
            bitmap.compress(imageFormat, quality, stream)
        return stream.toByteArray()
    }
    private fun defaultImageFormat(): Bitmap.CompressFormat{
       return if (Build.VERSION.SDK_INT >= 30){
            Bitmap.CompressFormat.WEBP_LOSSLESS
        }else {
            Bitmap.CompressFormat.WEBP
        }

    }
}