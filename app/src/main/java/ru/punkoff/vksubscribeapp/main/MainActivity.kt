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

        if (savedInstanceState == null) {
            val userId = intent.getParcelableExtra<UserId>(LoginActivity.EXTRA_USER_ID)
            viewModel.initVkApi(userId)
            viewModel.requestData()
        }
        with(binding) {
            collectFlow(viewModel.mainStateFlow) { viewState ->
                when (viewState) {
                    MainViewState.EMPTY -> Unit
                    is MainViewState.ERROR -> {
                        Log.e(javaClass.simpleName, viewState.exc.stackTraceToString())
                        progressBar.visibility = View.GONE
                    }
                    MainViewState.Loading -> progressBar.visibility = View.VISIBLE
                    is MainViewState.Success -> {
                        setAnimation(0, binding)
                        unsubscribeBtn.counter.text = "0"
                        unsubscribeBtn.progressBarBtn.visibility = View.GONE
                        unsubscribeBtn.counter.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                        communitiesAdapter.submitList(viewState.items)
                    }
                }
            }
        }
        setUpAdapter()
        setClickListeners()
    }

    private fun setUpAdapter() {
        with(binding) {
            communitiesAdapter.attachListener(object : OnItemClickListener {
                override fun onClick(subscription: Subscription) {
                    var count = Integer.parseInt(unsubscribeBtn.counter.text.toString())
                    if (subscription.isSelected) {
                        viewModel.addSubscription(subscription)
                        count++
                    } else {
                        viewModel.removeSubscription(subscription)
                        count--
                    }
                    setAnimation(count, binding)
                    unsubscribeBtn.counter.text = count.toString()
                }
            })
            communityList.layoutManager = GridLayoutManager(this@MainActivity, 3)
            communityList.adapter = communitiesAdapter
        }
    }

    fun setAnimation(count: Int, binding: ActivityMainBinding) {
        with(binding.unsubscribeBtn) {
            if (count == 0) {
                rootUnsubscribeBtn.startAnimation(
                    TranslateAnimation(
                        0f,
                        0f,
                        0f,
                        (rootUnsubscribeBtn.height + rootUnsubscribeBtn.marginBottom).toFloat()
                    ).apply {
                        duration = 100
                        fillAfter = true
                    })
            }
            if (count == 1 && counter.text == "0") {
                rootUnsubscribeBtn.visibility = View.VISIBLE
                rootUnsubscribeBtn.startAnimation(
                    TranslateAnimation(
                        0f,
                        0f,
                        (rootUnsubscribeBtn.height + rootUnsubscribeBtn.marginBottom).toFloat(),
                        0f
                    ).apply {
                        duration = 100
                        fillAfter = true
                    })
            }
        }
    }

    private fun setClickListeners() {
        with(binding.unsubscribeBtn) {
            rootUnsubscribeBtn.setOnClickListener {
                counter.visibility = View.INVISIBLE
                progressBarBtn.visibility = View.VISIBLE
                viewModel.leaveGroups()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}