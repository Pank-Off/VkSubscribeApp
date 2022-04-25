package ru.punkoff.vksubscribeapp.vk

import com.vk.dto.common.id.UserId
import com.vk.sdk.api.groups.dto.GroupsGetObjectExtendedResponse
import com.vk.sdk.api.groups.dto.GroupsGroupFull
import com.vk.sdk.api.wall.dto.WallGetExtendedResponse
import kotlinx.coroutines.flow.Flow
import ru.punkoff.vksubscribeapp.data.model.Subscription

interface VkApi {
    suspend fun getGroups(): Flow<GroupsGetObjectExtendedResponse>
    suspend fun leaveGroups(subscriptions: List<Subscription>)
    suspend fun joinGroups(subscriptions: List<Subscription>)
    suspend fun getLastPost(groupId: UserId?): WallGetExtendedResponse
    suspend fun getGroupById(groupId: UserId): List<GroupsGroupFull>
}
