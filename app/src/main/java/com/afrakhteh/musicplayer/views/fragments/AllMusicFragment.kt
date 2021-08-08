package com.afrakhteh.musicplayer.views.fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.FragmentAllMusicBinding

class AllMusicFragment : Fragment() {

private lateinit var binding: FragmentAllMusicBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentAllMusicBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

}