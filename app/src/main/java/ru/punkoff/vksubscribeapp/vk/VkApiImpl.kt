package ru.punkoff.vksubscribeapp.vk

import com.vk.api.sdk.VK
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.groups.GroupsService
import com.vk.sdk.api.groups.dto.GroupsFields
import com.vk.sdk.api.wall.WallService
import kotlinx.coroutines.flow.flow
import ru.punkoff.vksubscribeapp.model.Subscription

class VkApiImpl(private val userId: UserId?) {

    fun getGroups() = flow {
        val response = VK.executeSync(GroupsService().groupsGetExtended(userId = userId))
        emit(response)
    }

    fun leaveGroups(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            VK.executeSync(GroupsService().groupsLeave(it.groupId!!))
        }
    }

    fun joinGroups(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            VK.executeSync(GroupsService().groupsJoin(it.groupId))
        }
    }

    fun getLastPost(groupId: UserId?) =
        VK.executeSync(WallService().wallGetExtended(groupId, count = 2))

    fun getGroupById(groupId: UserId) =
        VK.executeSync(
            GroupsService().groupsGetById(
                listOf(groupId),
                fields = listOf(GroupsFields.DESCRIPTION, GroupsFields.MEMBERS_COUNT)
            )
        )
}