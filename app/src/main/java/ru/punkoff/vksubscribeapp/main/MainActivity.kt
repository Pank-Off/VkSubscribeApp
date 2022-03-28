package ru.punkoff.vksubscribeapp.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vk.dto.common.id.UserId
import ru.punkoff.vksubscribeapp.databinding.ActivityMainBinding
import ru.punkoff.vksubscribeapp.login.LoginActivity
import ru.punkoff.vksubscribeapp.utils.collectFlow

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val userId = intent.getParcelableExtra<UserId>(LoginActivity.EXTRA_USER_ID)
        viewModel.requestData(userId)

        collectFlow(viewModel.mainStateFlow) { viewState ->
            when (viewState) {
                MainViewState.EMPTY -> Unit
                is MainViewState.ERROR -> {
                    Log.e(javaClass.simpleName, viewState.exc.stackTraceToString())
                    binding.progressBar.visibility = View.GONE
                }
                MainViewState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is MainViewState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    viewState.items.forEach {
                        Log.i(javaClass.simpleName, "name - ${it.name}, photo - ${it.photo200}")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}