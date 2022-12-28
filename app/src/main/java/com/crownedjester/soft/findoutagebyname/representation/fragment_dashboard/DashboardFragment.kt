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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val visible = View.VISIBLE
private const val invisible = View.INVISIBLE
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

            searchView.query

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    //implement toast shown when search view is empty

                    dashboardViewModel.onEvent(
                        DashboardEvent.OnSubmitSearchQuery(query.toString())
                    )

                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    if (newText.isNullOrBlank()) {
                        this.onQueryTextSubmit("")
                    }

                    return true
                }

            })
        }

    }

    private fun onQuerySubmitted() {
        lifecycleScope.launch {
            dashboardViewModel.ageLiveData.observe(viewLifecycleOwner) { uiState ->
                when {
                    uiState.isLoading -> {
                        binding.apply {
                            loadingProgress.visibility = visible

                            buttonAddToFavorite.visibility = gone
                            buttonShare.visibility = gone

                            textViewResult.visibility = gone
                            textViewHint.visibility = gone
                        }
                    }

                    uiState.data != null -> {
                        binding.apply {
                            loadingProgress.visibility = gone

                            buttonAddToFavorite.visibility = visible
                            buttonShare.visibility = visible

                            textViewHint.visibility = gone
                            textViewResult.visibility = visible

                            textViewResult.text = uiState.data.age.toString()
                        }
                    }

                    uiState.message.isNotBlank() -> {
                        Log.e(TAG, uiState.message)
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

