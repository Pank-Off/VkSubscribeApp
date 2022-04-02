package ru.punkoff.vksubscribeapp.repository

import android.util.Log
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ru.punkoff.vksubscribeapp.bottomsheet.BottomSheetViewState
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.vk.VkApiImpl

class NetworkRepositoryImpl : NetworkRepository {

    private lateinit var vkApi: VkApiImpl

    override fun initVkApi(userId: UserId?) {
        vkApi = VkApiImpl(userId)
    }

    override fun getGroups() = flow<NetworkState> {
        vkApi.getGroups().collect {
            Log.i(javaClass.simpleName, "DataSize: ${it.count}")
            emit(NetworkState.Success(it.items))
        }

    }.catch {
        emit(NetworkState.Error(it))
    }

    override fun leaveGroups(subscriptions: List<Subscription>) {
        vkApi.leaveGroups(subscriptions)
    }

    override fun joinGroups(subscriptions: List<Subscription>) {
        vkApi.joinGroups(subscriptions)
    }

    override fun getLastPost(groupId: UserId?): BottomSheetViewState {
        val lastPost = vkApi.getLastPost(groupId)
        Log.e(javaClass.simpleName, "LastPost: ${lastPost.items[0].date}")
        val time: Long = lastPost.items[0].date!!.toLong()
        val correctTime = time * 1000
        Log.e(javaClass.simpleName, correctTime.toString())
        return BottomSheetViewState.Success(correctTime)
    }
}