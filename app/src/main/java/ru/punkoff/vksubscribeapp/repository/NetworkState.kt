package ru.punkoff.vksubscribeapp.repository

import com.vk.sdk.api.groups.dto.GroupsGroupFull

sealed class NetworkState {
    data class Error(val throwable: Throwable) : NetworkState()
    data class Success(val group: List<GroupsGroupFull>) : NetworkState()
}
