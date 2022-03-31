package ru.punkoff.vksubscribeapp.room

import androidx.room.TypeConverter
import com.vk.dto.common.id.UserId
import org.json.JSONObject

class SubscriptionTypeConverter {
    @TypeConverter
    fun subscriptionToJson(subscription: UserId?): String {
        val jsonObject = JSONObject()
        jsonObject.put("value", subscription?.value)
        return jsonObject.toString()
    }

    @TypeConverter
    fun jsonToSubscription(subscription: String): UserId {
        val jsonObject = JSONObject(subscription)
        val value = jsonObject.getLong("value")
        return UserId(value)
    }
}