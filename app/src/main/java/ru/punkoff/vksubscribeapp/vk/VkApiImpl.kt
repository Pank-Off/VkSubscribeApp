package ru.punkoff.vksubscribeapp.vk

import com.vk.api.sdk.VK
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.groups.GroupsService
import com.vk.sdk.api.groups.dto.GroupsFields
import com.vk.sdk.api.wall.WallService
import kotlinx.coroutines.flow.flow
import ru.punkoff.vksubscribeapp.data.model.Subscription
import javax.inject.Inject

class VkApiImpl @Inject constructor(private val userId: UserId?) : VkApi {

    override suspend fun getGroups() = flow {
        val response = VK.executeSync(GroupsService().groupsGetExtended(userId = userId))
        emit(response)
    }

    override suspend fun leaveGroups(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            VK.executeSync(GroupsService().groupsLeave(it.groupId!!))
        }
    }

    override suspend fun joinGroups(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            VK.executeSync(GroupsService().groupsJoin(it.groupId))
        }
    }

    override suspend fun getLastPost(groupId: UserId?) =
        VK.executeSync(WallService().wallGetExtended(groupId, count = 2))

    override suspend fun getGroupById(groupId: UserId) =
        VK.executeSync(
            GroupsService().groupsGetById(
                listOf(groupId),
                fields = listOf(GroupsFields.DESCRIPTION, GroupsFields.MEMBERS_COUNT)
            )
        )
}
