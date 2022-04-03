package ru.punkoff.vksubscribeapp.serviceprovider

import ru.punkoff.vksubscribeapp.repository.LocalRepositoryImpl
import ru.punkoff.vksubscribeapp.repository.NetworkRepositoryImpl
import ru.punkoff.vksubscribeapp.repository.RepositoryImpl
import ru.punkoff.vksubscribeapp.vk.VkApiImpl

object ServiceProvider {
    val vkApi = VkApiImpl()
    val networkRepository = NetworkRepositoryImpl()
    val localRepository = LocalRepositoryImpl()
    val repository = RepositoryImpl()
}