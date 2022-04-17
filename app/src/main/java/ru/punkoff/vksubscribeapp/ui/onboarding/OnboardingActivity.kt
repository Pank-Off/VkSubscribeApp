package ru.punkoff.vksubscribeapp.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.punkoff.vksubscribeapp.R
import ru.punkoff.vksubscribeapp.data.model.OnboardingModel
import ru.punkoff.vksubscribeapp.databinding.ActivityOnboardingBinding
import ru.punkoff.vksubscribeapp.ui.login.LoginActivity

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private var _binding: ActivityOnboardingBinding? = null
    private val binding: ActivityOnboardingBinding
        get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels()

    private lateinit var sliderAdapter: SliderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isOnboardingSeen()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding = ActivityOnboardingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initViewPager()
        initClickListeners()
    }

    private fun initViewPager() {
        val data = listOf(
            OnboardingModel(
                R.drawable.ic_launch_icon,
                getString(R.string.welcome),
                getString(R.string.welcome_subtitle)
            ),
            OnboardingModel(
                R.drawable.ic_check_circle_filled_blue_24,
                getString(R.string.unsubscribe_text),
                getString(R.string.onboarding_subscribe_description)
            ),
            OnboardingModel(
                R.drawable.ic_logo_vk_24,
                getString(R.string.onboarding_full_info_title),
                getString(R.string.onboarding_full_info_description)
            )
        )
        sliderAdapter = SliderAdapter(this, data)

        with(binding) {
            slideViewPager.adapter = sliderAdapter
            TabLayoutMediator(tabLayout, slideViewPager) { _, _ -> }.attach()
            // slideViewPager.setPageTransformer(ZoomOutPageTransformer())
            slideViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    when (state) {
                        SCROLL_STATE_DRAGGING -> {
                            viewModel.setTheButtonWasPressed(false)
                            viewModel.setSwipeStartedFlag(
                                true
                            )
                        }
                        SCROLL_STATE_SETTLING -> {
                            viewModel.setTheButtonWasPressed(false)
                            viewModel.setSwipeStartedFlag(
                                false
                            )
                        }
                        SCROLL_STATE_IDLE ->
                            if (viewModel.canFinishOnboarding()) finishOnboarding()
                    }
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    viewModel.setLastPageFlag(position == sliderAdapter.itemCount.minus(1))
                }
            })
        }
    }

    private fun initClickListeners() {
        with(binding) {
            nextBtn.setOnClickListener {
                viewModel.setLastPageFlag(
                    slideViewPager.currentItem == sliderAdapter.itemCount.minus(
                        1
                    )
                )
                viewModel.setTheButtonWasPressed(true)
                if (viewModel.canFinishOnboarding()) finishOnboarding()
                slideViewPager.setCurrentItem(slideViewPager.currentItem + 1, true)
            }
        }
    }

    private fun finishOnboarding() {
        viewModel.finishOnboarding()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
