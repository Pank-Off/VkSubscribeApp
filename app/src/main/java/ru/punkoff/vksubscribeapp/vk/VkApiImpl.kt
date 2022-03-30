package ru.punkoff.vksubscribeapp.vk

import com.vk.api.sdk.VK
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.groups.GroupsService
import kotlinx.coroutines.flow.flow
import ru.punkoff.vksubscribeapp.model.Subscription

class VkApiImpl(private val userId: UserId?) {

    fun getGroups() = flow {
        val response = VK.executeSync(GroupsService().groupsGet(userId = userId))
        emit(VK.executeSync(GroupsService().groupsGetById(response.items)))
    }

    fun leaveGroups(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            VK.executeSync(GroupsService().groupsLeave(it.id!!))
        }
    }

    fun joinGroups(subscriptions: List<Subscription>) {
        subscriptions.forEach {
            VK.executeSync(GroupsService().groupsJoin(it.id))
        }
    }
}