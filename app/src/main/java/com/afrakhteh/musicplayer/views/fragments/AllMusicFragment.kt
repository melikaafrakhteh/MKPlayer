package com.afrakhteh.musicplayer.views.fragments


import android.content.Context
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.afrakhteh.musicplayer.App
import com.afrakhteh.musicplayer.databinding.FragmentAllMusicBinding
import com.afrakhteh.musicplayer.viewModel.MainActivityViewModel
import com.afrakhteh.musicplayer.views.state.MusicState
import javax.inject.Inject

class AllMusicFragment : Fragment() {

    private lateinit var binding: FragmentAllMusicBinding
    @Inject lateinit var viewModel: MainActivityViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllMusicBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        (context.applicationContext as App).component.inject(this)
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(this,this::renderState)
        viewModel.fetchAllMusic()
    }
    private fun renderState(state: MusicState){
       state.message?.ifNotHandled {
           Toast.makeText(context,it,Toast.LENGTH_LONG).show()
       }

    }
}