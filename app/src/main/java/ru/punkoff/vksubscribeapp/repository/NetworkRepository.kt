package ru.punkoff.vksubscribeapp.repository

import com.vk.dto.common.id.UserId
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getGroups(userId: UserId?): Flow<NetworkState>
}