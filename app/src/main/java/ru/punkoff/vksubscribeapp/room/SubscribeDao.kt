package ru.punkoff.vksubscribeapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.vk.dto.common.id.UserId
import ru.punkoff.vksubscribeapp.model.Subscription

@Dao
interface SubscribeDao {
    @Insert(onConflict = REPLACE)
    fun insert(subscription: Subscription)

    @Query("DELETE FROM table_unsubscribed WHERE userId = :userId")
    fun delete(userId: UserId?)

    @Query("SELECT * FROM table_unsubscribed")
    fun getUnsubscribed(): List<Subscription>
}