package com.afrakhteh.musicplayer.views.main.customs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.RemoveItemDialogBinding

class DeleteItemsDialog(
        private val onDelete: () -> Unit
) : DialogFragment() {

    private lateinit var binding: RemoveItemDialogBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        binding = RemoveItemDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            deleteDialogCancelTv.setOnClickListener { dialog?.dismiss() }
            deleteDialogDeleteTv.setOnClickListener {
                onDelete.invoke()
                dialog?.dismiss()
            }
        }
    }
}