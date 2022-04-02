package ru.punkoff.vksubscribeapp.bottomsheet

import ru.punkoff.vksubscribeapp.model.SubscriptionInfo

sealed class BottomSheetViewState {
    object Loading : BottomSheetViewState()
    data class Success(val info: SubscriptionInfo) : BottomSheetViewState()
    data class ERROR(val exc: Throwable) : BottomSheetViewState()
}