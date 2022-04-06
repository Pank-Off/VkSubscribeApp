package ru.punkoff.vksubscribeapp.repository

import android.util.Log
import kotlinx.coroutines.flow.collect
import ru.punkoff.vksubscribeapp.bottomsheet.BottomSheetViewState
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.main.State
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.model.SubscriptionInfo
import ru.punkoff.vksubscribeapp.utils.Constants
import javax.inject.Inject

class RepositoryImpl
@Inject constructor(
    private val networkRepository: NetworkRepository,
    private val localRepository: LocalRepository
) : Repository {

    private val subscriptions = mutableListOf<Subscription>()

    override fun showUnsubscribed(): MainViewState = localRepository.getAll()

    override suspend fun getGroups(): MainViewState {

        val data = mutableListOf<Subscription>()
        var state: MainViewState = MainViewState.Loading

        networkRepository.getGroups().collect { viewState ->
            state = when (viewState) {
                is NetworkState.Error -> {
                    viewState.throwable.printStackTrace()
                    MainViewState.ERROR(State(viewState.throwable))
                }
                is NetworkState.Success -> {
                    viewState.group.forEach {
                        data.add(
                            Subscription(
                                it.id,
                                it.name,
                                it.photo100,
                            )
                        )

                        Log.i(
                            javaClass.simpleName,
                            "name - ${it.name}, photo - ${it.photo200}, membersCountText - ${it.membersCountText}"
                        )
                    }
                    MainViewState.Success(data)
                }
            }
        }

        Log.i(javaClass.simpleName, "DataSize: ${data.size}")

        return state
    }

    override fun addSubscription(subscription: Subscription) {
        subscriptions.add(subscription)
    }

    override fun removeSubscription(subscription: Subscription) {
        subscriptions.remove(subscription)
    }

    override suspend fun leaveGroups(): MainViewState = Result.runCatching {
        networkRepository.leaveGroups(subscriptions)
    }.fold({
        localRepository.insert(subscriptions)
        clearList()
        return getGroups()
    }, {
        return MainViewState.SubscribeError(State(it))
    })

    override suspend fun joinGroups(): MainViewState = Result.runCatching {
        networkRepository.joinGroups(subscriptions)
    }.fold({
        localRepository.delete(subscriptions)
        clearList()
        return showUnsubscribed()
    }, {
        return MainViewState.SubscribeError(State(it))
    })

    override fun getSubscriptionInfo(groupId: Long): BottomSheetViewState = Result.runCatching {
        val time = networkRepository.getLastPost(groupId)
        val group = networkRepository.getGroupById(groupId)[0]
        val url = "${Constants.VK_BASE_URL}${group.screenName}"
        SubscriptionInfo(
            group.membersCount,
            group.description,
            time,
            url
        )
    }.fold({
        return BottomSheetViewState.Success(it)
    }, {
        return BottomSheetViewState.ERROR(it)
    })

    override fun clearList() = subscriptions.clear()

    override fun getSubscriptionsSize() = subscriptions.size
}
