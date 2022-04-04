package ru.punkoff.vksubscribeapp.dagger

import com.vk.api.sdk.VK
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.punkoff.vksubscribeapp.vk.VkApi
import ru.punkoff.vksubscribeapp.vk.VkApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface VkModule {
    @Binds
    fun provideVkApi(vkApiImpl: VkApiImpl): VkApi

    companion object {
        @Provides
        @Singleton
        fun provideVkUserId() = VK.getUserId()
    }
}