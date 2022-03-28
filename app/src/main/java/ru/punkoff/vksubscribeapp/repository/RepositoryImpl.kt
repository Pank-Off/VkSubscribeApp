package ru.punkoff.vksubscribeapp.repository

import android.util.Log
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.groups.dto.GroupsGroupFull
import kotlinx.coroutines.flow.collect
import ru.punkoff.vksubscribeapp.main.MainViewState

class RepositoryImpl : Repository {

    private val networkRepository = NetworkRepositoryImpl()
    override suspend fun getGroups(userId: UserId?): MainViewState {
        val data = mutableListOf<GroupsGroupFull>()
        var state: MainViewState = MainViewState.EMPTY

        networkRepository.getGroups(userId).collect {
            when (it) {
                is NetworkState.Error -> {
                    it.throwable.printStackTrace()
                    state = MainViewState.ERROR(it.throwable)
                    return@collect
                }
                is NetworkState.Success -> data.addAll(it.group)
            }
        }

        if (state is MainViewState.ERROR) {
            return state
        }

        Log.i(javaClass.simpleName, "DataSize: ${data.size}")

        return MainViewState.Success(data)
    }
}