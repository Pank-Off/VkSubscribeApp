package ru.punkoff.vksubscribeapp.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.databinding.BottomSheetLayoutBinding
import ru.punkoff.vksubscribeapp.main.MainActivity
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.utils.collectFlow
import ru.punkoff.vksubscribeapp.utils.parseCount
import ru.punkoff.vksubscribeapp.utils.parseIntToDate

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
        viewModel.getLastPostTime(subscription.groupId)
        collectFlow(viewModel.bottomSheetStateFlow) {
            when (it) {
                is BottomSheetViewState.ERROR -> TODO()
                BottomSheetViewState.Loading -> {
                    binding.rootUnsubscribeBtn.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                }
                is BottomSheetViewState.Success -> {
                    Log.e(javaClass.simpleName, "Success!")
                    showData(subscription, it.lastPostTime)
                }
            }
        }
    }

    private fun showData(subscription: Subscription, state: Long?) {

        val count = parseCount(subscription.membersCount)
        val date = parseIntToDate(state)
        with(binding) {
            rootUnsubscribeBtn.isEnabled = true
            progressBar.visibility = View.GONE
            title.text = subscription.name
            membersCount.text = getString(R.string.members_count, count)
            description.text = subscription.description
            lastPost.text = getString(R.string.last_post, date)
            dismissBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance(bundle: Bundle) = BottomSheetFragment().apply {
            arguments = bundle
        }
    }
}