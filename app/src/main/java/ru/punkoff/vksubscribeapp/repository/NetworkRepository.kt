package ru.punkoff.vksubscribeapp.repository

import com.vk.sdk.api.groups.dto.GroupsGroupFull
import kotlinx.coroutines.flow.Flow
import ru.punkoff.vksubscribeapp.model.Subscription

interface NetworkRepository {
    fun getGroups(): Flow<NetworkState>
    fun leaveGroups(subscriptions: List<Subscription>)
    fun joinGroups(subscriptions: List<Subscription>)

    fun getLastPost(groupId: Long): Long
    fun getGroupById(groupId: Long): List<GroupsGroupFull>
}