package ru.punkoff.vksubscribeapp.bottomsheet

sealed class BottomSheetViewState {
    object Loading : BottomSheetViewState()
    data class Success(val lastPostTime: Long?) : BottomSheetViewState()
    data class ERROR(val exc: Throwable) : BottomSheetViewState()
}