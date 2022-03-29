package ru.punkoff.vksubscribeapp.repository

import com.vk.dto.common.id.UserId
import ru.punkoff.vksubscribeapp.main.MainViewState

interface Repository {
    suspend fun getGroups(userId: UserId?): MainViewState
}