package ru.punkoff.vksubscribeapp.ui.login

import com.vk.dto.common.id.UserId
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.punkoff.vksubscribeapp.base.BaseViewModel
import ru.punkoff.vksubscribeapp.data.SharedPreferenceManager
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val sharedPreferenceManager: SharedPreferenceManager) :
    BaseViewModel() {

    fun saveUserId(userId: UserId) {
        sharedPreferenceManager.saveUserId(userId)
    }

    fun getUserId(): UserId? = sharedPreferenceManager.getUserId()
}
