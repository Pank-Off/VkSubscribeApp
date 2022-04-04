package ru.punkoff.vksubscribeapp.repository

import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription

interface LocalRepository {
    fun insert(subscriptions: List<Subscription>)
    fun delete(subscriptions: List<Subscription>)
    fun getAll(): MainViewState
}
