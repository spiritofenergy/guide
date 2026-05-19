package com.kodex.guide.utils

import android.annotation.SuppressLint
import android.icu.util.Calendar
import java.text.SimpleDateFormat


@SuppressLint("ConstantLocale","SimpleDateFormat")
object TimeUtils {
    private  val timerFormatter = SimpleDateFormat("mm:ss")
    private  val postNameFormatter = SimpleDateFormat("dd/MM/yyyy mm:ss")

    fun getTimerTime(startTimerInMillis: Long): String {
        val elapsedTimeInMillis = System.currentTimeMillis() - startTimerInMillis
        val cv = Calendar.getInstance()
        cv.timeInMillis = elapsedTimeInMillis
        return timerFormatter.format(cv.time)
    }
    fun getDateAndTime(): String {
        val cv = Calendar.getInstance()
        return postNameFormatter.format(cv.time)
    }
}