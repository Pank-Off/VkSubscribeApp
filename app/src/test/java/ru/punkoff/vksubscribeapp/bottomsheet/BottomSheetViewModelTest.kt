package ru.punkoff.vksubscribeapp.bottomsheet

import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.punkoff.vksubscribeapp.model.SubscriptionInfo

@OptIn(ExperimentalCoroutinesApi::class)
class BottomSheetViewModelTest {

    private val _bottomSheetStateFlow =
        MutableStateFlow<BottomSheetViewState>(BottomSheetViewState.Loading)
    private val bottomSheetStateFlow = _bottomSheetStateFlow.asStateFlow()

    @Test
    fun `should success request`() = runTest(UnconfinedTestDispatcher()) {
        val values = mutableListOf<BottomSheetViewState>()
        val item = mockk<SubscriptionInfo>()

        val job = launch {
            bottomSheetStateFlow.collect {
                values.add(it)
            }
        }

        _bottomSheetStateFlow.value = BottomSheetViewState.Loading
        _bottomSheetStateFlow.value = BottomSheetViewState.Success(item)
        job.cancel()

        assertEquals(
            listOf(BottomSheetViewState.Loading, BottomSheetViewState.Success(item)),
            values
        )
    }

    @Test
    fun `should error request`() = runTest(UnconfinedTestDispatcher()) {
        val values = mutableListOf<BottomSheetViewState>()
        val error = mockk<Throwable>()
        val job = launch {
            bottomSheetStateFlow.collect {
                values.add(it)
            }
        }

        _bottomSheetStateFlow.value = BottomSheetViewState.Loading
        _bottomSheetStateFlow.value = BottomSheetViewState.ERROR(error)
        job.cancel()

        assertEquals(
            listOf(BottomSheetViewState.Loading, BottomSheetViewState.ERROR(error)),
            values
        )
    }
}
