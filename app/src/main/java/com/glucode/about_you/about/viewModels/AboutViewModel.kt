package com.glucode.about_you.about.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.glucode.about_you.common.SharedPreferenncesManager

class AboutViewModel(val context: Context): ViewModel() {
    private val sharedPref: SharedPreferenncesManager = SharedPreferenncesManager(context)

    fun saveImage(engineerName: String, uriString: String) {
        sharedPref.writeString(engineerName, uriString)
    }

    fun getImageUriString(engineerName: String) : String? {
        return sharedPref.readString(engineerName)
    }
}