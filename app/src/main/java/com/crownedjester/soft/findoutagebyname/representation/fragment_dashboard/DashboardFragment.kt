package com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.crownedjester.soft.findoutagebyname.R
import com.crownedjester.soft.findoutagebyname.databinding.FragmentDashboardBinding
import com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard.viewmodel.DashboardEvent
import com.crownedjester.soft.findoutagebyname.representation.fragment_dashboard.viewmodel.DashboardViewModel
import com.crownedjester.soft.findoutagebyname.representation.shared_components.MainEventHandlerViewModel
import com.crownedjester.soft.findoutagebyname.representation.shared_components.UiEvent
import dagger.hilt.android.AndroidEntryPoint

private const val visible = View.VISIBLE
private const val gone = View.GONE

private const val TAG = "DashboardFragment"

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel by viewModels<DashboardViewModel>()
    private val mainEventHandlerViewModel by activityViewModels<MainEventHandlerViewModel>()

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

                    // небольшой костыль :D
                    if (newText.isNullOrEmpty()) {
                        mainEventHandlerViewModel.sendEvent(
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
                        Log.e(TAG, uiState.message)
                    }

                }
            }
        }
    }


    @Suppress("Unused")
    private fun toDefaultFragmentState() {
        //todo not used
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
            loadingProgress.visibility = gone

            buttonAddToFavorite.visibility = visible
            buttonShare.visibility = visible

            textViewHint.visibility = gone
            textViewResult.visibility = visible

            textViewResult.text = uiState.data!!.age.toString()

            buttonAddToFavorite.setOnClickListener {
                dashboardViewModel.onEvent(
                    DashboardEvent.OnAddToFavorite(uiState.data)
                )
                mainEventHandlerViewModel.sendEvent(
                    UiEvent.ShowToast(
                        UiEvent.ShowToast.ToastType.SUCCESS,
                        getString(R.string.on_add_favorite_message)
                    )
                )
            }

            searchView.setQuery(uiState.data.name, false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

