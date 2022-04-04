package ru.punkoff.vksubscribeapp.vk

import com.vk.dto.common.id.UserId
import com.vk.sdk.api.groups.dto.GroupsGetObjectExtendedResponse
import com.vk.sdk.api.groups.dto.GroupsGroupFull
import com.vk.sdk.api.wall.dto.WallGetExtendedResponse
import kotlinx.coroutines.flow.Flow
import ru.punkoff.vksubscribeapp.model.Subscription

interface VkApi {
    fun getGroups(): Flow<GroupsGetObjectExtendedResponse>
    fun leaveGroups(subscriptions: List<Subscription>)
    fun joinGroups(subscriptions: List<Subscription>)
    fun getLastPost(groupId: UserId?): WallGetExtendedResponse
    fun getGroupById(groupId: UserId): List<GroupsGroupFull>
}