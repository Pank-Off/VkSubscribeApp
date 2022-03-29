package ru.punkoff.vksubscribeapp.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.GridLayoutManager
import com.vk.dto.common.id.UserId
import ru.punkoff.vksubscribeapp.databinding.ActivityMainBinding
import ru.punkoff.vksubscribeapp.login.LoginActivity
import ru.punkoff.vksubscribeapp.main.adapter.CommunitiesAdapter
import ru.punkoff.vksubscribeapp.main.adapter.OnItemClickListener
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.utils.collectFlow

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private val communitiesAdapter = CommunitiesAdapter()
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
                    val data = mutableListOf<Subscription>()
                    binding.progressBar.visibility = View.INVISIBLE
                    viewState.items.forEach {
                        data.add(Subscription(it.id, it.name, it.photo100))
                        Log.i(javaClass.simpleName, "name - ${it.name}, photo - ${it.photo200}")
                    }

                    communitiesAdapter.submitList(data)
                }
            }
        }
        setUpAdapter()
    }

    private fun setUpAdapter() {
        with(binding) {
            communitiesAdapter.attachListener(object : OnItemClickListener {
                override fun onClick(isSelected: Boolean) {
                    var count = Integer.parseInt(counter.text.toString())
                    if (isSelected) {
                        count++
                    } else {
                        count--
                    }
                    setAnimation(count, binding)
                    counter.text = count.toString()
                }
            })
            communityList.layoutManager = GridLayoutManager(this@MainActivity, 3)
            communityList.adapter = communitiesAdapter
        }
    }

    fun setAnimation(count: Int, binding: ActivityMainBinding) {
        with(binding) {
            if (count == 0) {
                unsubscribeBtn.startAnimation(
                    TranslateAnimation(
                        0f,
                        0f,
                        0f,
                        (unsubscribeBtn.height + unsubscribeBtn.marginBottom).toFloat()
                    ).apply {
                        duration = 100
                        fillAfter = true
                    })
            }
            if (count == 1 && counter.text == "0") {
                unsubscribeBtn.visibility = View.VISIBLE
                unsubscribeBtn.startAnimation(
                    TranslateAnimation(
                        0f,
                        0f,
                        (unsubscribeBtn.height + unsubscribeBtn.marginBottom).toFloat(),
                        0f
                    ).apply {
                        duration = 100
                        fillAfter = true
                    })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}