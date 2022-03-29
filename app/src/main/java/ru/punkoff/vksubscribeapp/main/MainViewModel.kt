package ru.punkoff.vksubscribeapp.main

import com.vk.dto.common.id.UserId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.punkoff.vksubscribeapp.base.BaseViewModel
import ru.punkoff.vksubscribeapp.repository.RepositoryImpl

class MainViewModel : BaseViewModel() {

    private val repo = RepositoryImpl()
    private val _mainStateFlow = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val mainStateFlow = _mainStateFlow.asStateFlow()

    fun requestData(userId: UserId?) {
        cancelJob()
        _mainStateFlow.value = MainViewState.Loading

        viewModelCoroutineScope.launch {
            _mainStateFlow.value = repo.getGroups(userId)
        }
    }
}