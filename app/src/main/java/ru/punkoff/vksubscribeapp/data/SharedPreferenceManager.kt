package ru.punkoff.vksubscribeapp.data

import android.content.Context
import android.content.SharedPreferences
import com.vk.dto.common.id.UserId
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject

interface SharedPreferenceManager {
    fun isOnboardingSeen(): Boolean
    fun setOnboardingSeen()
    fun saveUserId(userId: UserId)
    fun getUserId(): UserId?
}

class SharedPreferenceManagerImpl @Inject constructor(@ApplicationContext context: Context) :
    SharedPreferenceManager {

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(ONBOAR_PREFS, Context.MODE_PRIVATE)
    }

    override fun isOnboardingSeen(): Boolean {
        return preferences.contains(PREF_ONBOADRING_SEEN)
    }

    override fun setOnboardingSeen() {
        preferences.edit().putBoolean(PREF_ONBOADRING_SEEN, true).apply()
    }

    override fun saveUserId(userId: UserId) {
        val jsonObject = JSONObject()
        jsonObject.put("value", userId.value)
        preferences.edit().putString(PREF_USER_ID_KEY, jsonObject.toString()).apply()
    }

    override fun getUserId(): UserId? {
        val json = preferences.getString(PREF_USER_ID_KEY, null)
        json?.let {
            val jsonObject = JSONObject(it)
            val value = jsonObject.getLong("value")
            return UserId(value)
        }
        return null
    }

    companion object {
        const val ONBOAR_PREFS = "practicum_prefs"
        const val PREF_ONBOADRING_SEEN = "pref_onboadring_seen"
        const val PREF_USER_ID_KEY = "PREF_USER_ID_KEY"
    }
}
