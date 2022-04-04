package ru.punkoff.vksubscribeapp.repository

import ru.punkoff.vksubscribeapp.bottomsheet.BottomSheetViewState
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription

interface Repository {
    suspend fun getGroups(): MainViewState
    suspend fun leaveGroups(): MainViewState
    suspend fun joinGroups(): MainViewState

    fun addSubscription(subscription: Subscription)
    fun removeSubscription(subscription: Subscription)
    fun showUnsubscribed(): MainViewState

    fun clearList()
    fun getSubscriptionsSize(): Int
    fun getSubscriptionInfo(groupId: Long): BottomSheetViewState
}
