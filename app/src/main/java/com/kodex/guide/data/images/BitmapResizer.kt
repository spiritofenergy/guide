package com.kodex.guide.data.images

import android.graphics.Bitmap
import androidx.core.graphics.scale
import jakarta.inject.Singleton
import javax.inject.Inject

@Singleton
class BitmapResizer @Inject constructor() {
    fun resize(bitmap: Bitmap, maxSize: Int): Bitmap{
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