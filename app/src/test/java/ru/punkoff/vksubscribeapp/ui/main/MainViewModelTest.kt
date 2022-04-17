package ru.punkoff.vksubscribeapp.ui.main

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.punkoff.vksubscribeapp.data.model.Subscription
import ru.punkoff.vksubscribeapp.data.repository.Repository

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val _mainStateFlow = MutableStateFlow<MainViewState>(MainViewState.Loading)
    private val mainStateFlow = _mainStateFlow.asStateFlow()

    @MockK
    lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `should success request data`() = runTest(UnconfinedTestDispatcher()) {
        val values = mutableListOf<MainViewState>()
        val mockItem = mockk<List<Subscription>>()
        successRequest(mockItem)
        val job = launch {
            mainStateFlow.collect {
                values.add(it)
            }
        }

        _mainStateFlow.value = MainViewState.Loading
        repository.getGroups()

        job.cancel()

        assertEquals(
            listOf(MainViewState.Loading, MainViewState.Success(mockItem)),
            values
        )
    }

    private fun successRequest(mockItem: List<Subscription>) {
        every { runTest { repository.getGroups() } } answers {
            _mainStateFlow.value = MainViewState.Success(mockItem)
        }
    }

    @Test
    fun `should error request data`() = runTest(UnconfinedTestDispatcher()) {
        val values = mutableListOf<MainViewState>()
        val mockError = mockk<State<Throwable>>()
        errorRequest(mockError)
        val job = launch {
            mainStateFlow.collect {
                values.add(it)
            }
        }

        _mainStateFlow.value = MainViewState.Loading
        repository.getGroups()
        job.cancel()

        assertEquals(
            listOf(MainViewState.Loading, MainViewState.ERROR(mockError)),
            values
        )
    }

    private fun errorRequest(error: State<Throwable>) {
        every { runTest { repository.getGroups() } } answers {
            _mainStateFlow.value = MainViewState.ERROR(error)
        }
    }
}
