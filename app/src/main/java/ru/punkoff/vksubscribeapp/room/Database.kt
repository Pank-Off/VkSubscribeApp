package ru.punkoff.vksubscribeapp.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.punkoff.vksubscribeapp.model.Subscription

@Database(entities = [Subscription::class], version = 1, exportSchema = false)
@TypeConverters(
    SubscriptionTypeConverter::class
)
abstract class Database : RoomDatabase() {
    abstract fun subscribeDao(): SubscribeDao
}
