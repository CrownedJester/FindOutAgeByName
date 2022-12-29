package com.crownedjester.soft.findoutagebyname.representation.fragment_favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.crownedjester.soft.findoutagebyname.R
import com.crownedjester.soft.findoutagebyname.databinding.FragmentFavoriteBinding
import com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.adapter.NamesAdapter
import com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel by viewModels<FavoritesViewModel>()
    private val namesAdapter = NamesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFavoriteBinding.bind(view)

        binding.apply {
            rvFavoriteNames.apply {
                adapter = namesAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            }


        }

        lifecycleScope.launchWhenResumed {
            favoritesViewModel.favoriteNamesLiveData.observe(viewLifecycleOwner) {
                namesAdapter.differ.submitList(it)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}