package ru.punkoff.vksubscribeapp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.databinding.BottomSheetLayoutBinding
import ru.punkoff.vksubscribeapp.model.Subscription
import ru.punkoff.vksubscribeapp.utils.parseCount

class BottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetLayoutBinding? = null
    private val binding: BottomSheetLayoutBinding get() = _binding!!
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
        val count = parseCount(subscription.membersCount)
        with(binding) {
            title.text = subscription.name
            membersCount.text = getString(R.string.members_count, count)
            description.text = subscription.description
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