package ru.punkoff.vksubscribeapp.web

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import ru.punkoff.vksubscribeapp.bottomsheet.BottomSheetFragment
import ru.punkoff.vksubscribeapp.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        with(binding) {
            webView.webViewClient = VKWebViewClient()
            intent.getStringExtra(BottomSheetFragment.EXTRA_OPEN_WEB_ACTIVITY)
                ?.let { webView.loadUrl(it) }
        }
    }
}