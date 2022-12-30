package com.crownedjester.soft.findoutagebyname.representation.fragment_favorites.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.crownedjester.soft.findoutagebyname.R
import com.crownedjester.soft.findoutagebyname.databinding.DialogDeletionConfirmationBinding

class DeletionConfirmationDialog(private val callback: DeletionDialogCallback) :
    DialogFragment(R.layout.dialog_deletion_confirmation) {

    private var _binding: DialogDeletionConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DialogDeletionConfirmationBinding.bind(view)

        binding.apply {
            dialogConfirmBtn.setOnClickListener {
                callback.onConfirm()
            }

            dialogCancelBtn.setOnClickListener {
                callback.onCancel()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }
}