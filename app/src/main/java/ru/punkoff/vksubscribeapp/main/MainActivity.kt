package ru.punkoff.vksubscribeapp.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.bottomsheet.BottomSheetFragment
import ru.punkoff.vksubscribeapp.databinding.ActivityMainBinding
import ru.punkoff.vksubscribeapp.main.adapter.CommunitiesAdapter
import ru.punkoff.vksubscribeapp.main.adapter.OnItemClickListener
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.utils.collectFlow
import ru.punkoff.vksubscribeapp.utils.isOnline
import ru.punkoff.vksubscribeapp.utils.setTranslateAnimation

@AndroidEntryPoint
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
            viewModel.requestData()
        }

        with(binding) {
            collectFlow(viewModel.mainStateFlow) { viewState ->
                when (viewState) {
                    is MainViewState.ERROR -> {
                        val content = viewState.exc.getContentIfNotHandled()
                        content?.let {
                            showToastErrorMessage()
                            Log.e(javaClass.simpleName, content.stackTraceToString())
                        }
                        handleError()
                    }
                    MainViewState.Loading -> {
                        handleLoading()
                    }
                    is MainViewState.Success -> {
                        Log.e(javaClass.simpleName, "Success: ${viewState.items}")
                        showData()
                        if (viewState.items.isEmpty()) {
                            emptyListTv.visibility = View.VISIBLE
                        }
                        communitiesAdapter.submitList(viewState.items)
                    }
                    is MainViewState.SubscribeError -> {
                        val content = viewState.exc.getContentIfNotHandled()
                        content?.let {
                            showToastErrorMessage()
                            Log.e(javaClass.simpleName, it.stackTraceToString())
                        }
                        handleSubscribeError()
                    }
                    MainViewState.SubscribeLoading -> {
                        handleSubscribeLoading()
                    }
                }
            }
        }
        setUpAdapter()
        setClickListeners()
    }

    private fun handleError() {
        with(binding) {
            retryBtn.visibility = View.VISIBLE
            unsubscribeBtn.rootUnsubscribeBtn.visibility = View.GONE
            progressBar.visibility = View.GONE
            emptyListTv.visibility = View.GONE
        }
        communitiesAdapter.submitList(emptyList())
    }

    private fun handleLoading() {
        with(binding) {
            retryBtn.visibility = View.GONE
            unsubscribeBtn.rootUnsubscribeBtn.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            emptyListTv.visibility = View.GONE
        }
    }

    private fun showData() {
        setEnabled(true)
        val count = viewModel.getSubscriptionsSize()
        if (count == 0) {
            binding.unsubscribeBtn.rootUnsubscribeBtn.visibility = View.GONE
        }
        with(binding) {
            unsubscribeBtn.counter.text = count.toString()
            retryBtn.visibility = View.GONE
            emptyListTv.visibility = View.GONE
            unsubscribeBtn.progressBarBtn.visibility = View.GONE
            unsubscribeBtn.counter.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun handleSubscribeError() {
        setEnabled(true)
        val count = viewModel.getSubscriptionsSize()
        with(binding) {
            unsubscribeBtn.counter.text = count.toString()
            binding.unsubscribeBtn.counter.visibility = View.VISIBLE
            binding.unsubscribeBtn.progressBarBtn.visibility = View.INVISIBLE
        }
    }

    private fun showToastErrorMessage() {
        if (!isOnline(this@MainActivity)) {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.check_your_internet_message),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.something_went_wrong_text),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleSubscribeLoading() {
        setEnabled(false)
        binding.unsubscribeBtn.counter.visibility = View.INVISIBLE
        binding.unsubscribeBtn.progressBarBtn.visibility = View.VISIBLE
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

        outState.putParcelableArrayList(
            EXTRA_CURRENT_LIST,
            ArrayList(
                communitiesAdapter.currentList
            )
        )
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
            }
            if (communitiesAdapter.currentList.isEmpty()) {
                val currentList =
                    savedInstanceState.getParcelableArrayList<Subscription>(EXTRA_CURRENT_LIST)
                communitiesAdapter.submitList(currentList)
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
                    if (count == 0) {
                        setTranslateAnimation(false, unsubscribeBtn.rootUnsubscribeBtn)
                    } else if (count == 1 && unsubscribeBtn.counter.text == "0") {
                        setTranslateAnimation(true, unsubscribeBtn.rootUnsubscribeBtn)
                    }
                    unsubscribeBtn.counter.text = count.toString()
                }

                override fun onLongClick(subscription: Subscription) {
                    val bundle = Bundle()
                    bundle.putParcelable(
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

    private fun setClickListeners() {
        with(binding.unsubscribeBtn) {
            rootUnsubscribeBtn.setOnClickListener {
                if (binding.visibleBtn.isSelected) {
                    viewModel.joinGroups()
                } else {
                    viewModel.leaveGroups()
                }
            }
        }

        with(binding) {
            visibleBtn.setOnClickListener {
                viewModel.clearUnsubscribedList()
                it.isSelected = !it.isSelected

                if (it.isSelected) {
                    unsubscribeBtn.unsubscribeTvBtn.text = getString(R.string.subscribe)
                    unsubscribeTv.text = getString(R.string.subscribe_text)
                    viewModel.showUnsubscribed()
                } else {
                    unsubscribeBtn.unsubscribeTvBtn.text = getString(R.string.unsubscribe)
                    unsubscribeTv.text = getString(R.string.unsubscribe_text)
                    viewModel.requestData()
                }
                unsubscribeBtn.rootUnsubscribeBtn.requestLayout()
            }

            retryBtn.setOnClickListener {
                viewModel.requestData()
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
        const val EXTRA_CURRENT_LIST = "EXTRA_CURRENT_LIST"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
