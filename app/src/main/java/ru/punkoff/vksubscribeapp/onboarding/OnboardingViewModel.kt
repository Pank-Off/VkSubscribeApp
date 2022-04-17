package ru.punkoff.vksubscribeapp.onboarding

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import ru.punkoff.vksubscribeapp.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : BaseViewModel() {

    private val _isLastPage = MutableStateFlow(false)

    private val _isSwipeStarted = MutableStateFlow(false)
    private val _isTheButtonWasPressed = MutableStateFlow(false)
    fun setLastPageFlag(isLastPage: Boolean) {
        _isLastPage.value = isLastPage
    }

    fun canFinishOnboarding() =
        _isLastPage.value && (_isSwipeStarted.value || _isTheButtonWasPressed.value)

    fun setSwipeStartedFlag(isSwipeStarted: Boolean) {
        _isSwipeStarted.value = isSwipeStarted
    }

    fun setTheButtonWasPressed(isTheButtonWasPressed: Boolean) {
        _isTheButtonWasPressed.value = isTheButtonWasPressed
    }
}
