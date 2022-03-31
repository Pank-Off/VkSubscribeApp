package ru.punkoff.vksubscribeapp.app

import android.app.Application
import androidx.room.Room
import com.facebook.drawee.backends.pipeline.Fresco
import ru.punkoff.vksubscribeapp.room.Database
import ru.punkoff.vksubscribeapp.room.SubscribeDao

class VkApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)

        val db =
            Room.databaseBuilder(applicationContext, Database::class.java, "vk_subscription.db")
                .build()

        dao = db.subscribeDao()
    }

    companion object {
        lateinit var dao: SubscribeDao
    }
}