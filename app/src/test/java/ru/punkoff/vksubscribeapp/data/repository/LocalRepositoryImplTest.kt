package ru.punkoff.vksubscribeapp.data.repository

import com.vk.dto.common.id.UserId
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import ru.punkoff.vksubscribeapp.main.MainViewState
import ru.punkoff.vksubscribeapp.data.model.Subscription
import ru.punkoff.vksubscribeapp.data.room.SubscribeDao

@OptIn(ExperimentalCoroutinesApi::class)
class LocalRepositoryImplTest {

    @MockK(relaxed = true)
    lateinit var dao: SubscribeDao

    private lateinit var localRepository: LocalRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        localRepository = LocalRepositoryImpl(dao)
        every { dao.getUnsubscribed() } returns listOf(
            Subscription(UserId(123), "VK", "Uri", false, 666),
            Subscription(UserId(456), "Yandex", "Uri", false, 333),
            Subscription(UserId(789), "IGM", "Uri", false, 999)
        )
    }

    @Test
    fun insert() = runTest(UnconfinedTestDispatcher()) {

        coEvery { localRepository.getAll() } returns
            MainViewState.Success(dao.getUnsubscribed())

        val subscriptions = listOf(
            Subscription(UserId(123), "VK", "Uri", false, 666),
            Subscription(UserId(456), "Yandex", "Uri", false, 333),
            Subscription(UserId(789), "IGM", "Uri", false, 999)
        )
        localRepository.insert(subscriptions)

        val state = localRepository.getAll()

        //   assertEquals(subscriptions, state)
    }

    @Test
    fun delete() {
    }

    @Test
    fun getAll() {
    }
}
