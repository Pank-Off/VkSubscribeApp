package ru.punkoff.vksubscribeapp.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.databinding.BottomSheetLayoutBinding
import ru.punkoff.vksubscribeapp.main.MainActivity
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.model.SubscriptionInfo
import ru.punkoff.vksubscribeapp.utils.collectFlow
import ru.punkoff.vksubscribeapp.utils.parseCount
import ru.punkoff.vksubscribeapp.utils.parseIntToDate
import ru.punkoff.vksubscribeapp.web.WebActivity

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLayoutBinding? = null
    private val binding: BottomSheetLayoutBinding get() = _binding!!


    private val viewModel: BottomSheetViewModel by viewModels()

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
        val subscription =
            arguments?.get(MainActivity.KEY_FOR_SHOW_BOTTOM_SHEET_FRAGMENT) as Subscription
        viewModel.getSubscriptionInfo(subscription.groupId!!.value)
        collectFlow(viewModel.bottomSheetStateFlow) {
            when (it) {
                is BottomSheetViewState.ERROR -> {
                    binding.openWebPageBtn.isEnabled = false
                    Log.e(javaClass.simpleName, it.exc.stackTraceToString())
                    Toast.makeText(
                        context,
                        getString(R.string.something_went_wrong_text),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                BottomSheetViewState.Loading -> {
                    binding.openWebPageBtn.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
                is BottomSheetViewState.Success -> {
                    Log.e(javaClass.simpleName, "Success!")
                    showData(subscription, it.info)
                }
            }
        }
    }

    private fun showData(subscription: Subscription, subscriptionInfo: SubscriptionInfo) {

        val count = parseCount(subscriptionInfo.membersCount)
        val date = parseIntToDate(subscriptionInfo.lastPostDate)
        with(binding) {
            openWebPageBtn.isEnabled = true
            progressBar.visibility = View.GONE
            title.text = subscription.name
            membersCount.text = getString(R.string.members_count, count)
            description.text = subscriptionInfo.description
            lastPost.text = getString(R.string.last_post, date)

            openWebPageBtn.setOnClickListener {
                val intent = Intent(context, WebActivity::class.java)
                intent.putExtra(EXTRA_OPEN_WEB_ACTIVITY, subscriptionInfo.url)
                startActivity(intent)
            }
            dismissBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance(bundle: Bundle) = BottomSheetFragment().apply {
            arguments = bundle
        }

        const val EXTRA_OPEN_WEB_ACTIVITY = "EXTRA_OPEN_WEB_ACTIVITY"
    }
}