package ru.punkoff.vksubscribeapp.data.repository

import android.util.Log
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.groups.dto.GroupsGroupFull
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ru.punkoff.vksubscribeapp.data.model.Subscription
import ru.punkoff.vksubscribeapp.vk.VkApi
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(private val vkApi: VkApi) :
    NetworkRepository {

    override suspend fun getGroups() = flow<NetworkState> {
        vkApi.getGroups().collect {
            Log.i(javaClass.simpleName, "DataSize: ${it.count}")
            emit(NetworkState.Success(it.items))
        }
    }.catch {
        emit(NetworkState.Error(it))
    }

    override suspend fun leaveGroups(subscriptions: List<Subscription>) {
        vkApi.leaveGroups(subscriptions)
    }

    override suspend fun joinGroups(subscriptions: List<Subscription>) {
        vkApi.joinGroups(subscriptions)
    }

    override suspend fun getLastPost(groupId: Long): Long {
        val id = UserId(-groupId)
        val lastPost = vkApi.getLastPost(id)
        Log.e(javaClass.simpleName, "LastPost: ${lastPost.items[0].date}")
        val time: Long = lastPost.items[0].date!!.toLong()
        val correctTime = time * 1000
        Log.e(javaClass.simpleName, correctTime.toString())
        return correctTime
    }

    override suspend fun getGroupById(groupId: Long): List<GroupsGroupFull> =
        vkApi.getGroupById(UserId(groupId))
}
