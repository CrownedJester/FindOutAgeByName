package com.crownedjester.soft.findoutagebyname.representation.fragment_favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.crownedjester.soft.findoutagebyname.R
import com.crownedjester.soft.findoutagebyname.databinding.FragmentFavoriteBinding
import com.crownedjester.soft.findoutagebyname.features.model.FavoriteName
import com.crownedjester.soft.findoutagebyname.representation.common.bundle.BundlePrefs
import com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.adapter.NamesAdapter
import com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.adapter.NamesAdapterCallback
import com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.dialog.DeletionConfirmationDialog
import com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.dialog.DeletionDialogCallback
import com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.viewmodel.FavoritesViewModel
import com.crownedjester.soft.findoutagebyname.representation.shared_components.MainEventHandlerViewModel
import com.crownedjester.soft.findoutagebyname.representation.shared_components.UiEvent
import dagger.hilt.android.AndroidEntryPoint

private const val visible = View.VISIBLE
private const val invisible = View.INVISIBLE

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorite), NamesAdapterCallback,
    DeletionDialogCallback {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel by viewModels<FavoritesViewModel>()
    private val mainEventHandlerViewModel by activityViewModels<MainEventHandlerViewModel>()

    private val namesAdapter = NamesAdapter(this)

    private val dialog = DeletionConfirmationDialog(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFavoriteBinding.bind(view)

        lifecycleScope.launchWhenResumed {
            favoritesViewModel.favoriteNamesLiveData.observe(viewLifecycleOwner) {
                namesAdapter.differ.submitList(it)
            }
        }

        binding.apply {
            rvFavoriteNames.apply {
                adapter = namesAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            }

            buttonDeleteFavorite.setOnClickListener {
                dialog.show(
                    childFragmentManager,
                    DeletionConfirmationDialog::class.simpleName
                )
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

    override fun onLongClickCallback(adapterAction: () -> Unit) {
        binding.buttonDeleteFavorite.apply {
            visibility = if (visibility == visible) {
                invisible
            } else {
                visible
            }

            adapterAction()
        }
    }

    override fun onNameClickCallback(name: FavoriteName) {
        mainEventHandlerViewModel.sendEvent(
            UiEvent.OnNavigate(
                R.id.action_favoritesFragment_to_dashboardFragment,
                BundlePrefs.NAME_KEY,
                name.name
            )
        )
    }

    private fun deleteSelected() {
        namesAdapter.differ.currentList.forEach { name ->
            if (name.isSelected) {
                favoritesViewModel.deleteName(name)
            }
        }

    }

    override fun onConfirm() {
        deleteSelected()
        mainEventHandlerViewModel.sendEvent(
            UiEvent.ShowToast(
                type = UiEvent.ShowToast.ToastType.SUCCESS,
                message = getString(R.string.toast_on_delete)
            )
        )

        namesAdapter.namesHolderCallback?.performAdapterLongClick()
        dialog.dismiss()
    }

    override fun onCancel() {
        namesAdapter.namesHolderCallback?.performAdapterLongClick()
        dialog.dismiss()
    }

}