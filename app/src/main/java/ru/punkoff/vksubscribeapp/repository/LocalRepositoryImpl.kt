package ru.punkoff.vksubscribeapp.repository

import ru.punkoff.vksubscribeapp.app.VkApp
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription

class LocalRepositoryImpl : LocalRepository {
    private val dao = VkApp.dao
    override fun insert(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            it.isSelected = false
            dao.insert(it)
        }
    }

    override fun delete(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            dao.delete(it.groupId)
        }
    }

    override fun getAll(): MainViewState = MainViewState.Success(dao.getUnsubscribed())
}