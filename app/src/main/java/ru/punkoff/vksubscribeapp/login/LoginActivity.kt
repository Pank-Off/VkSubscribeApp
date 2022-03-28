package ru.punkoff.vksubscribeapp.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.utils.VKUtils.getCertificateFingerprint
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.main.MainActivity

class LoginActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER_ID = "EXTRA_USER_ID"
    }

    private val authLauncher = VK.login(this) { result: VKAuthenticationResult ->
        when (result) {
            is VKAuthenticationResult.Success -> {
                Log.i(javaClass.simpleName, "Success: ${result.token.userId.value}")
                val data = Intent(this, MainActivity::class.java)
                data.putExtra(EXTRA_USER_ID, result.token.userId)
                startActivity(data)
            }
            is VKAuthenticationResult.Failed -> {
                result.exception.stackTraceToString()
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signIn = findViewById<Button>(R.id.sign_in)
        val fingerprints = getCertificateFingerprint(this, this.packageName)!!

        Log.i(javaClass.simpleName, fingerprints[0].toString())
        signIn.setOnClickListener {
            authLauncher.launch(arrayListOf(VKScope.GROUPS))
        }
    }
}