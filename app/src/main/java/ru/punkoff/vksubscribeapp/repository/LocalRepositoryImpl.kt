package ru.punkoff.vksubscribeapp.repository

import android.util.Log
import kotlinx.coroutines.delay
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription

class LocalRepositoryImpl : LocalRepository {
    private val subscriptions = mutableListOf<Subscription>()
    override fun insert(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            it.isSelected = false
        }
        this.subscriptions.addAll(subscriptions)
        Log.e(javaClass.simpleName, "DataBase: ${this.subscriptions}")
    }

    override fun delete(subscriptions: List<Subscription>) {
        this.subscriptions.removeAll(subscriptions)
        Log.e(javaClass.simpleName, "DataBase: ${this.subscriptions}")
    }

    override fun getAll(): MainViewState = MainViewState.Success(subscriptions)
}