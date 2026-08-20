package com.kodex.guide.domain.service

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

interface WorkingHoursService {
    @RequiresApi(Build.VERSION_CODES.O)
    fun isOpenNow(openingHours: String, now: LocalTime = LocalTime.now()): Boolean

}