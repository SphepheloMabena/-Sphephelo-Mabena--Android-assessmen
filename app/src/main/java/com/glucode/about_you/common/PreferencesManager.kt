package com.glucode.about_you.common

import android.content.Context

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "settings")

class PreferencesManager {

    suspend fun editStringPref(context: Context, key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    fun getStringPref(context: Context, key: String): Flow<String?> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data
            .map { preferences ->
                preferences[prefKey]
            }
    }


    companion object {
        const val REENEN_IMAGE_KEY = "Reenen"
        const val WILMAR_IMAGE_KEY = "Wilmar"
        const val EBEN_IMAGE_KEY = "Eben"
        const val STEFAN_IMAGE_KEY = "STEFAN"
        const val BRANDON_IMAGE_KEY = "HENRI"
    }
}