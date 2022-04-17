package ru.punkoff.vksubscribeapp.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.punkoff.vksubscribeapp.data.model.OnboardingModel

class SliderAdapter(
    activity: OnboardingActivity,
    private val onboardingData: List<OnboardingModel>
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = onboardingData.size

    override fun createFragment(position: Int): Fragment {
        return OnboardingPageFragment.newInstance(
            imageRes = onboardingData[position].icon,
            title = onboardingData[position].title,
            subtitle = onboardingData[position].description
        )
    }
}
