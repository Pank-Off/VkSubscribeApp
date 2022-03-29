package ru.punkoff.vksubscribeapp.app

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class VkApp:Application() {

    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this);
    }
}