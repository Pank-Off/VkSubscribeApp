package ru.punkoff.vksubscribeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vk.dto.common.id.UserId
import java.io.Serializable

@Entity(tableName = "table_unsubscribed")
data class Subscription(
    val groupId: UserId?,
    val name: String?,
    val imageUri: String?,
    val membersCount: Int?,
    val description: String?,
    var isSelected: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) : Serializable