package ru.punkoff.vksubscribeapp.repository

import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.room.SubscribeDao
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val dao: SubscribeDao) : LocalRepository {

    override suspend fun insert(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            it.isSelected = false
            dao.insert(it)
        }
    }

    override suspend fun delete(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            dao.delete(it.groupId)
        }
    }

    override suspend fun getAll(): MainViewState = MainViewState.Success(dao.getUnsubscribed())
}
