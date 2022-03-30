package ru.punkoff.vksubscribeapp.repository

import android.util.Log
import ru.punkoff.vksubscribeapp.model.Subscription

class LocalRepositoryImpl : LocalRepository {
    private var subscriptions = mutableListOf<Subscription>()
    override fun insert(subscriptions: List<Subscription>) {
        this.subscriptions.addAll(subscriptions)

        Log.e(javaClass.simpleName, "DataBase: ${this.subscriptions}")
    }

    override fun getAll(): List<Subscription> = subscriptions
}