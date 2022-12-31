package com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.crownedjester.soft.findoutagebyname.R
import com.crownedjester.soft.findoutagebyname.databinding.FragmentDashboardBinding
import com.crownedjester.soft.findoutagebyname.domain.model.PersonData
import com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard.viewmodel.DashboardEvent
import com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard.viewmodel.DashboardViewModel
import com.crownedjester.soft.findoutagebyname.representation.shared_components.UiEvent
import com.crownedjester.soft.findoutagebyname.representation.shared_components.UiEventsHandlerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

private const val visible = View.VISIBLE
private const val gone = View.GONE

private const val TAG = "DashboardFragment"

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel by viewModels<DashboardViewModel>()
    private val uiEventsHandlerViewModel by activityViewModels<UiEventsHandlerViewModel>()

    private var backPressCallCount = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)

        setupBackPressedCallback()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDashboardBinding.bind(view)

        onQuerySubmitted()

        binding.apply {

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    dashboardViewModel.onEvent(
                        DashboardEvent.OnSubmitSearchQuery(query.toString())
                    )

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    // todo fix somehow
                    if (newText.isNullOrEmpty()) {
                        uiEventsHandlerViewModel.sendEvent(
                            UiEvent.ShowToast(
                                UiEvent.ShowToast.ToastType.WARNING,
                                getString(R.string.empty_search_query_message)
                            )
                        )
                    }

                    return false
                }

            })
        }

    }

    private fun onQuerySubmitted() {
        lifecycleScope.launchWhenResumed {
            dashboardViewModel.ageLiveData.observe(viewLifecycleOwner) { uiState ->
                when {
                    uiState.isLoading -> {
                        toLoadingState()
                    }

                    uiState.data != null -> {
                        toDataLoadedState(uiState)
                    }

                    uiState.message.isNotBlank() -> {
                        uiEventsHandlerViewModel.sendEvent(
                            UiEvent.ShowToast(
                                UiEvent.ShowToast.ToastType.ERROR,
                                getString(R.string.toast_get_data_error_message)
                            )
                        )
                        Log.e(TAG, uiState.message)
                    }

                }
            }
        }

    }


    @Suppress("Unused")
    private fun toDefaultFragmentState() {
        //todo not used yet
        binding.apply {
            loadingProgress.visibility = gone

            buttonAddToFavorite.visibility = gone
            buttonShare.visibility = gone

            textViewResult.visibility = gone
            textViewHint.visibility = visible
        }
    }

    private fun toLoadingState() {
        binding.apply {
            loadingProgress.visibility = visible

            buttonAddToFavorite.visibility = gone
            buttonShare.visibility = gone

            textViewResult.visibility = gone
            textViewHint.visibility = gone
        }
    }

    private fun toDataLoadedState(uiState: UiState) {
        binding.apply {

            searchView.setQuery(uiState.data!!.name, false)

            loadingProgress.visibility = gone

            buttonAddToFavorite.visibility = visible
            buttonShare.visibility = visible

            textViewHint.visibility = gone
            textViewResult.visibility = visible

            textViewResult.text = uiState.data.age.toString()

            buttonAddToFavorite.setOnClickListener {
                dashboardViewModel.onEvent(
                    DashboardEvent.OnAddToFavorite(uiState.data)
                )
                uiEventsHandlerViewModel.sendEvent(
                    UiEvent.ShowToast(
                        UiEvent.ShowToast.ToastType.SUCCESS,
                        getString(R.string.on_add_favorite_message)
                    )
                )
            }

            buttonShare.setOnClickListener {
                shareResult(uiState.data)
            }

        }
    }

    private fun shareResult(personData: PersonData) {

        val appName = with(requireContext()) {
            val stringId = applicationInfo.labelRes
            if (stringId == 0) {
                applicationInfo.nonLocalizedLabel.toString()
            } else {
                getString(stringId)
            }
        }

        val sendIntent = Intent().apply {

            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Привет! Ты можешь узнать свой настоящий возраст по имени в приложении " +
                        "$appName! " +
                        "Возраст для моего имени ${personData.name}\n такой - ${personData.age} лет"
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        requireContext().startActivity(shareIntent)

    }

    private fun setupBackPressedCallback() {

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                backPressCallCount++

                lifecycleScope.launch {
                    delay(1500L)
                    backPressCallCount = 0
                }

                if (backPressCallCount < 2) {
                    uiEventsHandlerViewModel.sendEvent(
                        UiEvent.ShowToast(
                            UiEvent.ShowToast.ToastType.INFO,
                            getString(R.string.info_on_back_pressed),
                            Toast.LENGTH_SHORT
                        )
                    )

                } else {
                    requireActivity().finish()
                    exitProcess(0)
                }
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

