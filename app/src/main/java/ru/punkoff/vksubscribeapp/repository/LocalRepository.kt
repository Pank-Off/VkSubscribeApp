package ru.punkoff.vksubscribeapp.repository

import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription

interface LocalRepository {
    suspend fun insert(subscriptions: List<Subscription>)
    suspend fun delete(subscriptions: List<Subscription>)
    suspend fun getAll(): MainViewState
}
