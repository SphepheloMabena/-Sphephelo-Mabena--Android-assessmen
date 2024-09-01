package com.glucode.about_you.common

import android.app.Activity
import android.content.Context
import android.provider.Settings.Global.getString
import com.glucode.about_you.R


class SharedPreferenncesManager(context: Context) {
    val sharedPref = context.getSharedPreferences(
        "prefd", Context.MODE_PRIVATE)


    fun writeString(key: String, value: String) {
        sharedPref?.let { with (sharedPref.edit()) {
            putString(key, value)
            apply()
        }
        }
    }

    fun readString(key: String) : String? {
        return sharedPref?.getString(key, "")
    }
}