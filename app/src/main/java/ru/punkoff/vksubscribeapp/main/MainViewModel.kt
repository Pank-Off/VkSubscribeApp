package ru.punkoff.vksubscribeapp.main

import com.vk.dto.common.id.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.punkoff.vksubscribeapp.base.BaseViewModel
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.repository.RepositoryImpl

class MainViewModel : BaseViewModel() {

    private val repo = RepositoryImpl()
    private val _mainStateFlow = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val mainStateFlow = _mainStateFlow.asStateFlow()

    fun initVkApi(userId: UserId?) {
        repo.initVkApi(userId)
    }

    fun requestData() {
        cancelJob()
        _mainStateFlow.value = MainViewState.Loading

        viewModelCoroutineScope.launch(Dispatchers.IO) {
            _mainStateFlow.value = repo.getGroups()
        }
    }

    fun addSubscription(subscription: Subscription) {
        repo.addSubscription(subscription)
    }

    fun removeSubscription(subscription: Subscription) {
        repo.removeSubscription(subscription)
    }

    fun leaveGroups() {
        cancelJob()
        viewModelCoroutineScope.launch(Dispatchers.IO) {
            _mainStateFlow.value = repo.leaveGroups()
        }
    }

    fun joinGroups() {
        cancelJob()
        viewModelCoroutineScope.launch(Dispatchers.IO) {
            _mainStateFlow.value = repo.joinGroups()
        }
    }

    fun showUnsubscribed() {
        cancelJob()
        viewModelCoroutineScope.launch(Dispatchers.IO) {
            _mainStateFlow.value = repo.showUnsubscribed()
        }
    }

    fun clearUnsubscribedList() {
        repo.clearList()
    }
}