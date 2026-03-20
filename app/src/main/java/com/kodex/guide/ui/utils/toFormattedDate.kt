package com.kodex.guide.ui.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun Long.toFormattedDate(pattern: String = "dd.MM.yyyy"): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}