package ru.punkoff.vksubscribeapp.repository

import com.vk.sdk.api.groups.dto.GroupsGroupFull
import kotlinx.coroutines.flow.Flow
import ru.punkoff.vksubscribeapp.model.Subscription

interface NetworkRepository {
    suspend fun getGroups(): Flow<NetworkState>
    suspend fun leaveGroups(subscriptions: List<Subscription>)
    suspend fun joinGroups(subscriptions: List<Subscription>)

    suspend fun getLastPost(groupId: Long): Long
    suspend fun getGroupById(groupId: Long): List<GroupsGroupFull>
}
