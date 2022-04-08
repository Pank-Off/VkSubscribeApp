package ru.punkoff.vksubscribeapp.room

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.punkoff.vksubscribeapp.model.Subscription

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class SubscribeDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: Database

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            Database::class.java
        ).build()
    }

    @Test
    fun insertSubscriptionAndGet() {
        val subscription = Subscription(UserId(123), "VK", "Uri", false, 666)
        database.subscribeDao().insert(subscription)

        val loaded = database.subscribeDao().getUnsubscribed()[0]

        assertThat(loaded.id, `is`(subscription.id))
        assertThat(loaded.groupId, `is`(subscription.groupId))
        assertThat(loaded.name, `is`(subscription.name))
        assertThat(loaded.imageUri, `is`(subscription.imageUri))
        assertThat(loaded.isSelected, `is`(subscription.isSelected))
    }

    @Test
    fun deleteSubscription() {
        val subscription = Subscription(UserId(123), "VK", "Uri", false, 666)
        database.subscribeDao().insert(subscription)

        database.subscribeDao().delete(subscription.groupId)
        val loaded = database.subscribeDao().getUnsubscribed()
        assert(loaded.isEmpty())
    }

    @After
    fun closeDb() = database.close()
}
