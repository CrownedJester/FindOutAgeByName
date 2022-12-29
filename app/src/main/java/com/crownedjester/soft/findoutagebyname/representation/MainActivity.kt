package com.crownedjester.soft.findoutagebyname.representation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
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
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

private const val TAG = "MainActivity_Log"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val eventHandlerViewModel by viewModels<MainEventHandlerViewModel>()
    private var navController by Delegates.notNull<NavController>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configToasty()

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)

        lifecycleScope.launch {
            whenStarted {
                eventHandlerViewModel.uiEvent.collect { uiEvent ->
                    handleUiEvents(uiEvent)
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

    private fun handleUiEvents(uiEvent: UiEvent) {
        when (uiEvent) {
            is UiEvent.OnBack -> {
                navController.navigateUp()
                Log.d(TAG, "OnBack event Handled")
            }

            is UiEvent.OnNavigate<*> -> {
                uiEvent.apply {
                    val (routeId, key, arg) = uiEvent
                    navigate(routeId, key, arg)
                    Log.d(TAG, "OnNavigate event Handled")
                }
            }

            is UiEvent.ShowToast -> {
                showToast(uiEvent.type, uiEvent.message)
                Log.d(TAG, "ShowToast event Handled")
            }
        }
    }

    private fun navigate(routeId: Int, argKey: String? = null, arg: Any? = null) {
        if (argKey.isNullOrEmpty() && arg == null) {
            navController.navigate(routeId)
        } else {
            argKey?.let { key ->
                navController.navigate(
                    routeId,
                    bundleOf(key to arg)
                )
            }
        }
    }

    private fun showToast(type: UiEvent.ShowToast.ToastType, message: String) {
        when (type) {
            UiEvent.ShowToast.ToastType.ERROR ->
                Toasty.error(applicationContext, message, Toast.LENGTH_LONG).show()

            UiEvent.ShowToast.ToastType.INFO ->
                Toasty.info(this@MainActivity, message, Toast.LENGTH_LONG).show()

            UiEvent.ShowToast.ToastType.NORMAL ->
                Toasty.normal(this@MainActivity, message, Toast.LENGTH_LONG).show()

            UiEvent.ShowToast.ToastType.SUCCESS ->
                Toasty.success(applicationContext, message, Toast.LENGTH_LONG).show()

            UiEvent.ShowToast.ToastType.WARNING ->
                Toasty.warning(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun configToasty() {
        Toasty.Config.getInstance()
            .setTextSize(16)
            .allowQueue(false)
            .supportDarkTheme(false)
            .apply()
    }


}