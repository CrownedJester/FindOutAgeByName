package com.crownedjester.soft.findoutagebyname.representation.fragment_favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.crownedjester.soft.findoutagebyname.R
import com.crownedjester.soft.findoutagebyname.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorite) {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFavoriteBinding.bind(view)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}