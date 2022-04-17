package ru.punkoff.vksubscribeapp.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.punkoff.vksubscribeapp.databinding.OnboardingPageFragmentBinding

class OnboardingPageFragment : Fragment() {

    private var _binding: OnboardingPageFragmentBinding? = null
    private val binding: OnboardingPageFragmentBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OnboardingPageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            arguments?.getString(ARGS_TITLE)?.let { title.text = it }
            arguments?.getString(ARGS_SUBTITLE)?.let { subtitle.text = it }
            arguments?.getInt(ARGS_IMAGE_RES)?.let { image.setImageResource(it) }
        }
    }

    companion object {
        private const val ARGS_IMAGE_RES = "args_image_res"
        private const val ARGS_TITLE = "args_title"
        private const val ARGS_SUBTITLE = "args_subtitle"

        fun newInstance(imageRes: Int?, title: String?, subtitle: String?): OnboardingPageFragment {
            return OnboardingPageFragment().apply {
                arguments = Bundle().apply {
                    imageRes?.let { putInt(ARGS_IMAGE_RES, imageRes) }
                    title?.let { putString(ARGS_TITLE, title) }
                    subtitle?.let { putString(ARGS_SUBTITLE, subtitle) }
                }
            }
        }
    }
}
