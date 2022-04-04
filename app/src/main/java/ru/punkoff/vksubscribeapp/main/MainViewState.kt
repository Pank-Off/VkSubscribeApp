package ru.punkoff.vksubscribeapp.main

import ru.punkoff.vksubscribeapp.model.Subscription

sealed class MainViewState {
    object Loading : MainViewState()
    object SubscribeLoading : MainViewState()
    data class SubscribeError(val exc: Throwable) : MainViewState()
    data class Success(val items: List<Subscription>) : MainViewState()
    data class ERROR(val exc: Throwable) : MainViewState()
}
