package ru.punkoff.vksubscribeapp.data.room

import androidx.room.TypeConverter
import com.vk.dto.common.id.UserId
import org.json.JSONObject

class SubscriptionTypeConverter {
    @TypeConverter
    fun subscriptionToJson(userId: UserId?): String {
        val jsonObject = JSONObject()
        jsonObject.put("value", userId?.value)
        return jsonObject.toString()
    }

    @TypeConverter
    fun jsonToSubscription(userId: String): UserId {
        val jsonObject = JSONObject(userId)
        val value = jsonObject.getLong("value")
        return UserId(value)
    }
}
