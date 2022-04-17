package ru.punkoff.vksubscribeapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vk.dto.common.id.UserId
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "table_unsubscribed")
data class Subscription(
    @ColumnInfo(name = "userId")
    val groupId: UserId?,
    val name: String?,
    val imageUri: String?,
    var isSelected: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) : Parcelable
