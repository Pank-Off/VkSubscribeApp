package ru.punkoff.vksubscribeapp.repository

import android.util.Log
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import ru.punkoff.vksubscribeapp.vk.VkApiImpl

class NetworkRepositoryImpl : NetworkRepository {

    lateinit var vkApi: VkApiImpl
    override fun getGroups(userId: UserId?) = flow<NetworkState> {

        vkApi = VkApiImpl(userId)
        vkApi.getGroups()

        delay(3000)
        val data = vkApi.listOfGroupsInfo

        Log.i(javaClass.simpleName, "DataSize: ${data?.size}")
        data?.let {
            emit(NetworkState.Success(it))
        }
    }.catch {
        emit(NetworkState.Error(it))
    }
}