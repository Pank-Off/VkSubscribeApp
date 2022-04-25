package ru.punkoff.vksubscribeapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.utils.VKUtils.getCertificateFingerprint
import com.vk.dto.common.id.UserId
import dagger.hilt.android.AndroidEntryPoint
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.ui.main.MainActivity
import ru.punkoff.vksubscribeapp.utils.isOnline

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
    }

    private var signInBtn: CardView? = null
    private val viewModel: LoginViewModel by viewModels()

    private val authLauncher = VK.login(this) { result: VKAuthenticationResult ->
        when (result) {
            is VKAuthenticationResult.Success -> {
                val userId = result.token.userId
                Log.i(javaClass.simpleName, "Success: ${userId.value}")
                viewModel.saveUserId(userId)
                startMainActivity(userId)
            }
            is VKAuthenticationResult.Failed -> {
                Log.e(javaClass.simpleName, "Failed: ${result.exception.stackTraceToString()}")
                if (!isOnline(this)) {
                    Toast.makeText(
                        this,
                        getString(R.string.check_your_internet_message),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.something_went_wrong_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInBtn = findViewById(R.id.sign_in_btn)
        signInBtn?.setOnClickListener {
            authLauncher.launch(arrayListOf(VKScope.GROUPS))
        }
        val fingerprints = getCertificateFingerprint(this, this.packageName)!!
        Log.i(javaClass.simpleName, fingerprints[0].toString())

        val userId = viewModel.getUserId()
        if (savedInstanceState == null && userId == null) {
            authLauncher.launch(arrayListOf(VKScope.GROUPS))
        } else if (userId != null) {
            startMainActivity(userId)
        }
    }

    private fun startMainActivity(userId: UserId) {
        val data = Intent(this, MainActivity::class.java)
        data.putExtra(EXTRA_USER_ID, userId)
        startActivity(data)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        signInBtn = null
    }
}
