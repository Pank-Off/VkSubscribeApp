package ru.punkoff.vksubscribeapp.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface SharedPreferenceManager {
    fun isOnboadingSeen(): Boolean
    fun setOnboadingSeen()
}

class SharedPreferenceManagerImpl @Inject constructor(@ApplicationContext context: Context) : SharedPreferenceManager {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(ONBOAR_PREFS, Context.MODE_PRIVATE)
    }

    override fun isOnboadingSeen(): Boolean {
        return preferences.contains(PREF_ONBOADRING_SEEN)
    }

    override fun setOnboadingSeen() {
        preferences.edit().putBoolean(PREF_ONBOADRING_SEEN, true).apply()
    }

    companion object {
        const val ONBOAR_PREFS = "practicum_prefs"
        const val PREF_ONBOADRING_SEEN = "pref_onboadring_seen"
    }
}
