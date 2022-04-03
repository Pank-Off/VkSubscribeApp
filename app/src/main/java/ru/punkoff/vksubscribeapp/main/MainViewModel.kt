package ru.punkoff.vksubscribeapp.main

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.punkoff.vksubscribeapp.base.BaseViewModel
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.repository.Repository
import ru.punkoff.vksubscribeapp.serviceprovider.ServiceProvider

class MainViewModel(private val repo: Repository = ServiceProvider.repository) : BaseViewModel() {

    private val _mainStateFlow = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val mainStateFlow = _mainStateFlow.asStateFlow()

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

    fun getSubscriptionsSize(): Int = repo.getSubscriptionsSize()
}