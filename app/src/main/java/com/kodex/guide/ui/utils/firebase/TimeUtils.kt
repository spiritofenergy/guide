package com.kodex.guide.ui.utils.firebase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {

        fun gerFormatTime(time: Long): String {

            val data = Date(time)
            val format = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault())
            return format.format(data)
        }

}