package ru.punkoff.vksubscribeapp.bottomsheet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.punkoff.vksubscribeapp.base.BaseViewModel
import ru.punkoff.vksubscribeapp.repository.RepositoryImpl

class BottomSheetViewModel : BaseViewModel() {
    private val repo = RepositoryImpl()
    private val _bottomSheetStateFlow =
        MutableStateFlow<BottomSheetViewState>(BottomSheetViewState.Loading)
    val bottomSheetStateFlow = _bottomSheetStateFlow.asStateFlow()

    init {
        repo.initVkApi(null)
    }

    fun getSubscriptionInfo(groupId: Long) {
        cancelJob()
        _bottomSheetStateFlow.value = BottomSheetViewState.Loading
        viewModelCoroutineScope.launch(Dispatchers.IO) {
            _bottomSheetStateFlow.value = repo.getSubscriptionInfo(groupId)
        }
    }
}