package ru.punkoff.vksubscribeapp.bottomsheet

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.punkoff.vksubscribeapp.base.BaseViewModel
import ru.punkoff.vksubscribeapp.repository.Repository
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(private val repo: Repository) :
    BaseViewModel() {

    private val _bottomSheetStateFlow =
        MutableStateFlow<BottomSheetViewState>(BottomSheetViewState.Loading)
    val bottomSheetStateFlow = _bottomSheetStateFlow.asStateFlow()

    fun getSubscriptionInfo(groupId: Long) {
        cancelJob()
        _bottomSheetStateFlow.value = BottomSheetViewState.Loading
        viewModelCoroutineScope.launch(Dispatchers.IO) {
            _bottomSheetStateFlow.value = repo.getSubscriptionInfo(groupId)
        }
    }
}