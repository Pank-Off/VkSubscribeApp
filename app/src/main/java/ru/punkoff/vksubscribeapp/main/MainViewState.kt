package ru.punkoff.vksubscribeapp.main

import com.vk.sdk.api.groups.dto.GroupsGroupFull

sealed class MainViewState {
    object EMPTY : MainViewState()
    object Loading : MainViewState()
    data class Success(val items: List<GroupsGroupFull>) : MainViewState()
    data class ERROR(val exc: Throwable) : MainViewState()
}