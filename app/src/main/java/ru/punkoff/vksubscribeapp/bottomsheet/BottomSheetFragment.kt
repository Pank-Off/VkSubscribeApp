package ru.punkoff.vksubscribeapp.bottomsheet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.databinding.BottomSheetLayoutBinding
import ru.punkoff.vksubscribeapp.main.MainActivity
import ru.punkoff.vksubscribeapp.data.model.Subscription
import ru.punkoff.vksubscribeapp.data.model.SubscriptionInfo
import ru.punkoff.vksubscribeapp.utils.collectFlow
import ru.punkoff.vksubscribeapp.utils.isOnline
import ru.punkoff.vksubscribeapp.utils.parseCount
import ru.punkoff.vksubscribeapp.utils.parseLongToDate

@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLayoutBinding? = null
    private val binding: BottomSheetLayoutBinding get() = _binding!!

    private val viewModel: BottomSheetViewModel by viewModels()

    private lateinit var subscription: Subscription
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscription =
            arguments?.get(MainActivity.KEY_FOR_SHOW_BOTTOM_SHEET_FRAGMENT) as Subscription
        binding.title.text = subscription.name
        binding.dismissBtn.setOnClickListener {
            dismiss()
        }
        viewModel.getSubscriptionInfo(subscription.groupId!!.value)
        collectFlow(viewModel.bottomSheetStateFlow) {
            when (it) {
                is BottomSheetViewState.ERROR -> {
                    Log.e(javaClass.simpleName, it.exc.stackTraceToString())
                    if (!isOnline(context)) {
                        showError(getString(R.string.check_your_internet_message))
                    } else {
                        showError(getString(R.string.something_went_wrong_text))
                    }
                }
                BottomSheetViewState.Loading -> {
                    with(binding) {
                        openTvBtn.text = getString(R.string.open)
                        errorMsg.visibility = View.GONE
                        openWebPageBtn.isEnabled = false
                        progressBar.visibility = View.VISIBLE
                    }
                }
                is BottomSheetViewState.Success -> {
                    binding.errorMsg.visibility = View.GONE
                    Log.e(javaClass.simpleName, "Success!")
                    showData(it.info)
                }
            }
        }
    }

    private fun showError(errorText: String) {
        with(binding) {
            openWebPageBtn.isEnabled = true
            progressBar.visibility = View.GONE
            lastPost.visibility = View.INVISIBLE
            description.visibility = View.INVISIBLE
            membersCount.visibility = View.INVISIBLE
            errorMsg.visibility = View.VISIBLE
            errorMsg.text = errorText
            openTvBtn.text = getString(R.string.retry_text)
            openWebPageBtn.setOnClickListener {
                viewModel.getSubscriptionInfo(subscription.groupId!!.value)
            }
        }
    }

    private fun showData(subscriptionInfo: SubscriptionInfo) {

        val count = parseCount(subscriptionInfo.membersCount)
        val date = parseLongToDate(subscriptionInfo.lastPostDate)
        with(binding) {
            openWebPageBtn.isEnabled = true
            progressBar.visibility = View.GONE
            membersCount.text = getString(R.string.members_count, count)
            description.text = subscriptionInfo.description
            lastPost.text = getString(R.string.last_post, date)
            lastPost.visibility = View.VISIBLE
            description.visibility = View.VISIBLE
            membersCount.visibility = View.VISIBLE
            openWebPageBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(subscriptionInfo.url))
                startActivity(intent)
            }
        }
    }

    companion object {
        fun newInstance(bundle: Bundle) = BottomSheetFragment().apply {
            arguments = bundle
        }
    }
}
