package com.sdn.teampredators.polima.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.ui.utils.DataResult
import com.sdn.teampredators.polima.ui.utils.ListResult
import com.sdn.teampredators.polima.ui.utils.glide
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUsersData()

    }

    private fun getUsersData() {
        viewModel.getUsers().observe(this) {
            when (it) {
                is ListResult.Success -> {
                    Timber.d("this is user data ${it.list}")
                    Toast.makeText(this, "${it.list}", Toast.LENGTH_LONG).show()
                }
                is ListResult.Error -> {
                    Timber.d("${it.error}")
                }
                is ListResult.Loading -> {
                    Timber.d("Loading...")
                    Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}