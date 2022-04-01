package ru.punkoff.vksubscribeapp.repository

import com.vk.dto.common.id.UserId
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription

interface Repository {
    suspend fun getGroups(): MainViewState
    suspend fun leaveGroups(): MainViewState
    suspend fun joinGroups(): MainViewState

    fun addSubscription(subscription: Subscription)
    fun removeSubscription(subscription: Subscription)
    fun initVkApi(userId: UserId?)
    fun showUnsubscribed(): MainViewState

    fun clearList()
    fun getSubscriptionsSize(): Int
}