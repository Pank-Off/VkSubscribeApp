package ru.punkoff.vksubscribeapp.main

import android.content.res.Configuration
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
import ru.punkoff.vksubscribeapp.R
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

        if (viewModel.getSubscriptionsSize() == 0) {
            binding.unsubscribeBtn.rootUnsubscribeBtn.visibility = View.GONE
        }

        with(binding) {
            collectFlow(viewModel.mainStateFlow) { viewState ->
                when (viewState) {
                    is MainViewState.ERROR -> {
                        Log.e(javaClass.simpleName, viewState.exc.stackTraceToString())
                        unsubscribeBtn.rootUnsubscribeBtn.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        emptyListTv.visibility = View.GONE
                    }
                    MainViewState.Loading -> {
                        setAnimation(0, binding)
                        unsubscribeBtn.rootUnsubscribeBtn.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        emptyListTv.visibility = View.GONE
                    }
                    is MainViewState.Success -> {
                        setEnabled(true)
                        Log.e(javaClass.simpleName, "Success: ${viewState.items}")
                        emptyListTv.visibility = View.GONE
                        val count = viewModel.getSubscriptionsSize()
                        setAnimation(count, binding)
                        unsubscribeBtn.counter.text = count.toString()
                        unsubscribeBtn.progressBarBtn.visibility = View.GONE
                        unsubscribeBtn.counter.visibility = View.VISIBLE
                        progressBar.visibility = View.INVISIBLE
                        if (viewState.items.isEmpty()) {
                            emptyListTv.visibility = View.VISIBLE
                        }
                        communitiesAdapter.submitList(viewState.items)
                    }
                }
            }
        }
        setUpAdapter()
        setClickListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        with(binding) {
            outState.putString(EXTRA_TITLE_INFO_TEXT, unsubscribeTv.text.toString())
            outState.putString(
                EXTRA_BUTTON_TEXT,
                unsubscribeBtn.unsubscribeTvBtn.text.toString()
            )
            outState.putBoolean(EXTRA_VISIBLE_BUTTON, visibleBtn.isSelected)
            outState.putBundle(EXTRA_MOTION_LAYOUT_STATE, container.transitionState)
            outState.putBoolean(
                EXTRA_REQUEST_IS_STILL_IN_PROGRESS,
                unsubscribeBtn.rootUnsubscribeBtn.isEnabled
            )
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        with(binding) {
            unsubscribeTv.text = savedInstanceState.getString(EXTRA_TITLE_INFO_TEXT)
            unsubscribeBtn.unsubscribeTvBtn.text =
                savedInstanceState.getString(EXTRA_BUTTON_TEXT)
            visibleBtn.isSelected = savedInstanceState.getBoolean(EXTRA_VISIBLE_BUTTON)
            container.transitionState =
                savedInstanceState.getBundle(EXTRA_MOTION_LAYOUT_STATE)
            if (!savedInstanceState.getBoolean(EXTRA_REQUEST_IS_STILL_IN_PROGRESS)) {
                unsubscribeBtn.counter.visibility = View.GONE
                unsubscribeBtn.progressBarBtn.visibility = View.VISIBLE
                setEnabled(false)
            }
        }
    }

    private fun setUpAdapter() {
        with(binding) {
            communitiesAdapter.attachListener(object : OnItemClickListener {
                override fun onClick(subscription: Subscription) {
                    if (subscription.isSelected) {
                        viewModel.addSubscription(subscription)
                    } else {
                        viewModel.removeSubscription(subscription)
                    }

                    val count = viewModel.getSubscriptionsSize()
                    setAnimation(count, binding)
                    unsubscribeBtn.counter.text = count.toString()
                }

                override fun onLongClick(subscription: Subscription) {
                    val bundle = Bundle()
                    bundle.putSerializable(
                        KEY_FOR_SHOW_BOTTOM_SHEET_FRAGMENT,
                        subscription
                    )
                    BottomSheetFragment.newInstance(bundle)
                        .show(supportFragmentManager, "choose_fragment")
                }
            })

            val layoutManager =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(this@MainActivity, 5)
                } else {
                    GridLayoutManager(this@MainActivity, 3)
                }
            communityList.layoutManager = layoutManager
            communityList.adapter = communitiesAdapter
        }
    }

    fun setAnimation(count: Int, binding: ActivityMainBinding) {
        with(binding.unsubscribeBtn) {
            if (count == 0) {
                rootUnsubscribeBtn.visibility = View.VISIBLE
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
                rootUnsubscribeBtn.visibility = View.GONE
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
                setEnabled(false)
                counter.visibility = View.INVISIBLE
                progressBarBtn.visibility = View.VISIBLE
                if (binding.visibleBtn.isSelected) {
                    viewModel.joinGroups()
                } else {
                    viewModel.leaveGroups()
                }
            }
        }

        with(binding) {
            binding.visibleBtn.setOnClickListener {
                viewModel.clearUnsubscribedList()
                it.isSelected = !it.isSelected

                if (it.isSelected) {
                    unsubscribeBtn.unsubscribeTvBtn.text = getString(R.string.subscribe)
                    unsubscribeTv.text = getString(R.string.subscribe_text)
                    unsubscribeBtn.rootUnsubscribeBtn.requestLayout()
                    viewModel.showUnsubscribed()
                } else {
                    unsubscribeBtn.unsubscribeTvBtn.text = getString(R.string.unsubscribe)
                    unsubscribeTv.text = getString(R.string.unsubscribe_text)
                    unsubscribeBtn.rootUnsubscribeBtn.requestLayout()
                    viewModel.requestData()
                }
            }
        }
    }

    private fun setEnabled(isEnabled: Boolean) {
        binding.unsubscribeBtn.rootUnsubscribeBtn.isEnabled = isEnabled
        binding.visibleBtn.isEnabled = isEnabled
        communitiesAdapter.setEnabled(isEnabled)
    }

    companion object {
        const val EXTRA_BUTTON_TEXT = "EXTRA_BUTTON_TEXT"
        const val EXTRA_TITLE_INFO_TEXT = "EXTRA_TITLE_INFO_TEXT"
        const val EXTRA_VISIBLE_BUTTON = "EXTRA_VISIBLE_BUTTON"
        const val EXTRA_MOTION_LAYOUT_STATE = "EXTRA_MOTION_LAYOUT_STATE"
        const val EXTRA_REQUEST_IS_STILL_IN_PROGRESS = "EXTRA_REQUEST_IS_STILL_IN_PROGRESS"
        const val KEY_FOR_SHOW_BOTTOM_SHEET_FRAGMENT = "KEY_FOR_SHOW_BOTTOM_SHEET_FRAGMENT"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}