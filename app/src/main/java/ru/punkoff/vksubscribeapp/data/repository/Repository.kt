package ru.punkoff.vksubscribeapp.data.repository

import ru.punkoff.vksubscribeapp.bottomsheet.BottomSheetViewState
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.data.model.Subscription

interface Repository {
    suspend fun getGroups(): MainViewState
    suspend fun leaveGroups(): MainViewState
    suspend fun joinGroups(): MainViewState
    suspend fun showUnsubscribed(): MainViewState
    suspend fun getSubscriptionInfo(groupId: Long): BottomSheetViewState

    fun addSubscription(subscription: Subscription)
    fun removeSubscription(subscription: Subscription)

    fun clearList()
    fun getSubscriptionsSize(): Int
}
