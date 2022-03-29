package ru.punkoff.vksubscribeapp.model

import com.vk.dto.common.id.UserId

data class Subscription(
    val id: UserId?,
    val name: String?,
    val imageUri: String?,
    var isSelected: Boolean = false
)