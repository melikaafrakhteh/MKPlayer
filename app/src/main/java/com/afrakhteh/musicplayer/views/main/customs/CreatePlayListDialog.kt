package com.afrakhteh.musicplayer.views.main.customs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.PlaylistCreateNewDialogBinding

class CreatePlayListDialog(
        private val onInputSelected: (String) -> Unit
) : DialogFragment() {

    private lateinit var binding: PlaylistCreateNewDialogBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.round_corner)
        binding = PlaylistCreateNewDialogBinding.inflate(
                layoutInflater, container, false
        )
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            newPlayListCreateDialogCancelTv.setOnClickListener { dialog?.dismiss() }
            newPlayListCreateDialogOkTv.setOnClickListener {
                val input = newPlayListCreateDialogET.text.toString().trim()
                if (input.isEmpty()) {
                    Toast.makeText(
                            requireContext(),
                            getString(R.string.empty_playlist_name_msg),
                            Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                onInputSelected.invoke(input)
                dialog?.dismiss()
            }
        }
    }
}