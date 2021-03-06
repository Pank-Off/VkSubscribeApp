package ru.punkoff.vksubscribeapp.data.repository

import ru.punkoff.vksubscribeapp.data.model.Subscription
import ru.punkoff.vksubscribeapp.ui.main.MainViewState

interface LocalRepository {
    suspend fun insert(subscriptions: List<Subscription>)
    suspend fun delete(subscriptions: List<Subscription>)
    suspend fun getAll(): MainViewState
}
