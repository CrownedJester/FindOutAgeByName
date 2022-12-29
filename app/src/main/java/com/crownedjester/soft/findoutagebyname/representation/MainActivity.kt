package com.crownedjester.soft.findoutagebyname.representation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.crownedjester.soft.findoutagebyname.R
import com.crownedjester.soft.findoutagebyname.representation.shared_components.MainEventHandlerViewModel
import com.crownedjester.soft.findoutagebyname.representation.shared_components.UiEvent
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val eventHandlerViewModel by viewModels<MainEventHandlerViewModel>()
    private var navController by Delegates.notNull<NavController>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        lifecycleScope.launch {
            whenStarted {
                eventHandlerViewModel.uiEvent.collect { uiEvent ->
                    when (uiEvent) {
                        is UiEvent.OnBack -> {
                            navController.navigateUp()
                        }

                        is UiEvent.OnNavigate<*> -> {
                            uiEvent.apply {
                                if (key.isNullOrEmpty() && arg == null) {
                                    navController.navigate(routeId)
                                } else {
                                    //todo nav with args
                                }
                            }
                        }

                        is UiEvent.ShowToast -> {
                            Toast.makeText(
                                this@MainActivity,
                                uiEvent.message,
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    }

                }
            }

        }

    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    @Suppress("Deprecation")
    override fun onBackPressed() {
        super.onBackPressed()


    }

}