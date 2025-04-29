package com.kodex.guide.ui.utils

import android.content.Context
import javax.inject.Singleton

@Singleton
class StoreManager(
    context: Context
) {
    private val pref = context.getSharedPreferences(MAIN_PREF, Context.MODE_PRIVATE)
    fun saveString(key: String,value: String){
        pref.edit().putString(key, value).apply()
    }

    fun getString(key: String, defValue: String): String{
        return pref.getString(key, defValue)?: defValue
    }
    companion object{
        const val MAIN_PREF = "main_pref"
        const val EMAIL_KEY = "email_key"
    }
}