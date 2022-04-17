package ru.punkoff.vksubscribeapp.main

import ru.punkoff.vksubscribeapp.data.model.Subscription

sealed class MainViewState {
    object Loading : MainViewState()
    object SubscribeLoading : MainViewState()
    data class SubscribeError(val exc: State<Throwable>) : MainViewState()
    data class Success(val items: List<Subscription>) : MainViewState()
    data class ERROR(val exc: State<Throwable>) : MainViewState()
}
