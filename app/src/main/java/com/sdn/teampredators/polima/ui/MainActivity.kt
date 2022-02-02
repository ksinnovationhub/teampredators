package com.sdn.teampredators.polima.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.sdn.teampredators.polima.R
import com.sdn.teampredators.polima.databinding.ActivityMainBinding
import com.sdn.teampredators.polima.utils.viewBinding
import com.sdn.teampredators.polima.utils.viewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val navController: NavController by lazy {
        Navigation.findNavController(
            this,
            R.id.nav_host_fragment_container
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking { delay(1_500) }

        setTheme(R.style.Polima)
        setContentView(binding.root)
        setUpBottomNav()
    }

    private fun setUpBottomNav() = with(binding) {
        bottomNavigationView.apply {
            setupWithNavController(navController)
            setOnItemReselectedListener { }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home, R.id.notificationFragment, R.id.userProfileFragment -> {
                    showBottomNavigation()
                    homeStatusBar()
                }
                R.id.aspirantFragment -> {
                    hideBottomNavigation()
                    aspirantStatusBar()
                }
                R.id.profileFragment -> {
                    hideBottomNavigation()
                    homeStatusBar()
                }
                else -> hideBottomNavigation()
            }
        }
    }

    private fun showBottomNavigation() = with(binding) {
        bottomNavigationView.viewState(true)
        TransitionManager.beginDelayedTransition(
            binding.root,
            Slide(Gravity.END).excludeTarget(R.id.nav_host_fragment_container, true)
        )
    }

    private fun hideBottomNavigation() = with(binding) {
        TransitionManager.beginDelayedTransition(
            binding.root,
            Slide(Gravity.BOTTOM).excludeTarget(R.id.nav_host_fragment_container, true)
        )
        bottomNavigationView.viewState(false)
    }

    private fun homeStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.polima_green)
    }

    private fun aspirantStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.white)
    }
}
