package ru.punkoff.vksubscribeapp.repository

import android.util.Log
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.flow.collect
import ru.punkoff.vksubscribeapp.bottomsheet.BottomSheetViewState
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.model.SubscriptionInfo
import ru.punkoff.vksubscribeapp.utils.Constants

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
        var state: MainViewState = MainViewState.Loading

        networkRepository.getGroups().collect { viewState ->
            state = when (viewState) {
                is NetworkState.Error -> {
                    viewState.throwable.printStackTrace()
                    MainViewState.ERROR(viewState.throwable)
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

    override suspend fun leaveGroups(): MainViewState {
        networkRepository.leaveGroups(subscriptions)
        localRepository.insert(subscriptions)
        clearList()
        return getGroups()
    }

    override suspend fun joinGroups(): MainViewState {
        networkRepository.joinGroups(subscriptions)
        localRepository.delete(subscriptions)
        clearList()
        return showUnsubscribed()
    }

    override fun getSubscriptionInfo(groupId: Long): BottomSheetViewState {
        val time = networkRepository.getLastPost(groupId)
        val group = networkRepository.getGroupById(groupId)[0]
        val url = "${Constants.VK_BASE_URL}${group.screenName}"
        return BottomSheetViewState.Success(
            SubscriptionInfo(
                group.membersCount,
                group.description,
                time,
                url)
        )
    }

    override fun clearList() = subscriptions.clear()

    override fun getSubscriptionsSize() = subscriptions.size
}