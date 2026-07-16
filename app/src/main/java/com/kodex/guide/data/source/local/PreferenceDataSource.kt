package com.kodex.guide.data.source.local

import android.content.Context
import androidx.core.content.edit
import javax.inject.Singleton

@Singleton
class PreferenceDataSource(
    context: Context
) {
    private val pref = context.getSharedPreferences(MAIN_PREF, Context.MODE_PRIVATE)
    fun saveEmail(key: String, value: String){
        pref.edit { putString(key, value) }
    }

    fun getEmail(key: String, defValue: String): String{
        return pref.getString(key, defValue)?: defValue
    }
    companion object{
        const val MAIN_PREF = "main_pref"
        const val EMAIL_KEY = "email_key"
    }
}