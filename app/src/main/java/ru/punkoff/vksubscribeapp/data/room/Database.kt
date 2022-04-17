package ru.punkoff.vksubscribeapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.punkoff.vksubscribeapp.data.model.Subscription

@Database(entities = [Subscription::class], version = 1, exportSchema = false)
@TypeConverters(
    SubscriptionTypeConverter::class
)
abstract class Database : RoomDatabase() {
    abstract fun subscribeDao(): SubscribeDao
}
