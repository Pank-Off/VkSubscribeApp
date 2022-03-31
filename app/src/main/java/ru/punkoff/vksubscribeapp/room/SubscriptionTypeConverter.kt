package ru.punkoff.vksubscribeapp.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.vk.dto.common.id.UserId

class SubscriptionTypeConverter {
    @TypeConverter
    fun subscriptionToJson(subscription: UserId?): String = Gson().toJson(subscription)

    @TypeConverter
    fun jsonToSubscription(subscription: String): UserId? =
        Gson().fromJson(subscription, UserId::class.java)
}