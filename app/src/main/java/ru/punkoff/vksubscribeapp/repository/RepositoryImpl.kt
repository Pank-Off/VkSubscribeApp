package ru.punkoff.vksubscribeapp.repository

import android.util.Log
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.flow.collect
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription
import java.util.*

class RepositoryImpl : Repository {

    private val networkRepository = NetworkRepositoryImpl()
    private val localRepository = LocalRepositoryImpl()
    private val subscriptions = mutableListOf<Subscription>()
    override fun initVkApi(userId: UserId?) {
        networkRepository.initVkApi(userId)
    }

    override fun showUnsubscribed(): MainViewState = localRepository.getAll()

    override suspend fun getGroups(): MainViewState {

        val data = mutableListOf<Subscription>()
        var state: MainViewState = MainViewState.EMPTY

        networkRepository.getGroups().collect { viewState ->
            when (viewState) {
                is NetworkState.Error -> {
                    viewState.throwable.printStackTrace()
                    state = MainViewState.ERROR(viewState.throwable)
                    return@collect
                }
                is NetworkState.Success -> {
                    viewState.group.forEach {
                        data.add(Subscription(it.id, it.name, it.photo100))
                        Log.i(javaClass.simpleName, "name - ${it.name}, photo - ${it.photo200}")
                    }
                }
            }
        }

        if (state is MainViewState.ERROR) {
            return state
        }

        Log.i(javaClass.simpleName, "DataSize: ${data.size}")

        return MainViewState.Success(data)
    }

    override fun addSubscription(subscription: Subscription) {
        subscriptions.add(subscription)
    }

    override fun removeSubscription(subscription: Subscription) {
        subscriptions.remove(subscription)
    }

    override suspend fun leaveGroups(): MainViewState {
        networkRepository.leaveGroups(subscriptions)
        localRepository.insert(subscriptions)
        subscriptions.clear()
        return getGroups()
    }

    override suspend fun joinGroups(): MainViewState {
        networkRepository.joinGroups(subscriptions)
        localRepository.delete(subscriptions)
        subscriptions.clear()
        return showUnsubscribed()
    }
}