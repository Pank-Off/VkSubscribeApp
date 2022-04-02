package ru.punkoff.vksubscribeapp.repository

import com.vk.dto.common.id.UserId
import kotlinx.coroutines.flow.Flow
import ru.punkoff.vksubscribeapp.bottomsheet.BottomSheetViewState
import ru.punkoff.vksubscribeapp.model.Subscription

interface NetworkRepository {
    fun initVkApi(userId: UserId?)
    fun getGroups(): Flow<NetworkState>
    fun leaveGroups(subscriptions: List<Subscription>)
    fun joinGroups(subscriptions: List<Subscription>)

    fun getLastPost(groupId: UserId?): BottomSheetViewState
}