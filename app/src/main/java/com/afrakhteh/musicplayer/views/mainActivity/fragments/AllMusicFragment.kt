package com.afrakhteh.musicplayer.views.mainActivity.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.FragmentAllMusicBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.model.dataSource.AudioDecodingListener
import com.afrakhteh.musicplayer.model.dataSource.AudioWaveDataSource
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.viewModel.MainActivityViewModel
import com.afrakhteh.musicplayer.views.mainActivity.adapters.allMusic.AllMusicAdapter
import com.afrakhteh.musicplayer.views.mainActivity.interfaces.PermissionController
import com.afrakhteh.musicplayer.views.mainActivity.state.MusicState

import javax.inject.Inject


class AllMusicFragment : Fragment() {

    private lateinit var binding: FragmentAllMusicBinding

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private val viewModel: MainActivityViewModel by viewModels { providerFactory }

    private lateinit var musicAdapter: AllMusicAdapter


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllMusicBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        ViewModelComponentBuilder.getInstance().inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialiseView()
        initialiseViewModel()

        (requireActivity() as PermissionController).apply {
            if (hasPermission()) {
                viewModel.fetchAllMusic()
                setOnPermissionRequestCallBack(this@AllMusicFragment::onPermissionGranted)
            } else {
                requestPermission()
            }
        }
    }

    private fun initialiseView() {
        musicAdapter = AllMusicAdapter(this::onMusicItemClicked)
        binding.allFragmentRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = musicAdapter
        }
    }

    private fun onMusicItemClicked(data: MusicEntity) {
        Toast.makeText(requireContext(), data.name, Toast.LENGTH_LONG).show()

        val dataSource = AudioWaveDataSource()
        // val path = "android.resource://com.afrakhteh.musicplayer/raw/sample"
        dataSource.readAudio(data.path, object : AudioDecodingListener {
            override fun isCanceled(): Boolean {
                Log.e("AllMusic", "isCanceled")
                return false
            }

            override fun onStartProcessing(duration: Long, channelsCount: Int, sampleRate: Int) {
                Log.e("AllMusic", "start")
            }

            override fun onProcessingProgress(percent: Int) {
                Log.e("AllMusic", "onProcessingProgress")
            }

            override fun onProcessingCancel() {
                Log.e("AllMusic", "onProcessingCancel")
            }

            override fun onFinishProcessing(data: ArrayList<Int>, duration: Long) {
                Log.e("AllMusic", data.toString())
            }

            override fun onError(exception: Exception) {
                Log.e("AllMusic", exception.toString() + "helllooooooo")
            }

        })
    }

    private fun initialiseViewModel() {
        viewModel.state.observe(requireActivity(), this::renderState)
    }

    @SuppressLint("SetTextI18n")
    private fun renderState(state: MusicState) {
        state.message?.ifNotHandled {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        musicAdapter.submitList(ArrayList(state.musicItems))
        val number = state.musicItems.size
        binding.allFragmentNumberTv.text = "$number songs"
    }

    private fun onPermissionGranted(permission: Boolean) {
        if (permission) {
            viewModel.fetchAllMusic()
        } else {
            Toast.makeText(requireContext(), getString(R.string.deny_message), Toast.LENGTH_LONG)
                .show()
        }
    }
}