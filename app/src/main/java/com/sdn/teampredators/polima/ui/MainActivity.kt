package com.sdn.teampredators.polima.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.ActivityMainBinding
import com.sdn.teampredators.polima.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking { delay(1_500) }

        setTheme(R.style.Polima)
        setContentView(binding.root)
    }
}
