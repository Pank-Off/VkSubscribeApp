package ru.punkoff.vksubscribeapp.vk

import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.groups.GroupsService
import com.vk.sdk.api.groups.dto.GroupsGetResponse
import com.vk.sdk.api.groups.dto.GroupsGroupFull

class VkApiImpl(private val userId: UserId?) {

    var listOfGroupsInfo: List<GroupsGroupFull>? = null
    fun getGroups() {
        VK.execute(GroupsService().groupsGet(userId = userId), object :
            VKApiCallback<GroupsGetResponse> {
            override fun fail(error: Exception) {
                error.stackTraceToString()
            }

            override fun success(result: GroupsGetResponse) {
                getGroupsInfo(result)
            }
        })
    }

    fun getGroupsInfo(result: GroupsGetResponse) {
        VK.execute(GroupsService().groupsGetById(result.items), object :
            VKApiCallback<List<GroupsGroupFull>> {
            override fun fail(error: Exception) {
                error.stackTraceToString()
            }

            override fun success(result: List<GroupsGroupFull>) {
                listOfGroupsInfo = result
            }
        })
    }
}