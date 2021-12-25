package com.afrakhteh.musicplayer.views.main.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.databinding.FragmentRecentlyBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.viewModel.RecentlyAddedViewModel
import com.afrakhteh.musicplayer.views.main.adapters.recently.RecentlyAdapter
import com.afrakhteh.musicplayer.views.main.state.MusicState
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import javax.inject.Inject

class RecentlyFragment : Fragment() {

    private lateinit var binding: FragmentRecentlyBinding

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory
    private val viewModel: RecentlyAddedViewModel by viewModels { providerFactory }

    private lateinit var recentlyAdapter: RecentlyAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecentlyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        ViewModelComponentBuilder.getInstance().inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isPermissionGranted = arguments?.getBoolean(Strings.CHECK_PERMISSION_NOTIFY_RECENTLY_KEY)
        if (isPermissionGranted != null) {
            onPermissionGranted(isPermissionGranted)
        }

        recentlyAdapter = RecentlyAdapter(::onItemClicked, viewModel.repository)
        binding.recentlyFragmentRecycler.adapter = recentlyAdapter
        viewModel.recentlyAddedState.observe(viewLifecycleOwner, ::onItemRecentlyAdded)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchRecentlyAddedMusic()
    }

    private fun onPermissionGranted(permission: Boolean) {
        if (permission) {
            viewModel.fetchRecentlyAddedMusic()
        } else {
            Toast.makeText(requireContext(), getString(R.string.deny_message), Toast.LENGTH_LONG)
                    .show()
        }
    }

    private fun onItemClicked(position: Int) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java).apply {
            putExtra(Strings.AUDIO_ACTIVE_POSITION__KEY, position)
            putParcelableArrayListExtra(Strings.AUDIO_All_MUSIC_LIST_KEY, ArrayList(audioPrePareToPlayList()))
        }
        startActivity(intent)
    }

    private fun audioPrePareToPlayList(): List<AudioPrePareToPlay>? {
        return viewModel.recentlyAddedState.value?.musicItems?.map { musicEntity ->
            AudioPrePareToPlay(
                    id = musicEntity.index,
                    path = musicEntity.path,
                    musicName = musicEntity.name ?: "",
                    musicArtist = musicEntity.artist ?: ""
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onItemRecentlyAdded(musicState: MusicState?) {
        musicState?.message?.ifNotHandled {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        recentlyAdapter.submitList(ArrayList(musicState?.musicItems))
        binding.recentlyFragmentNumberTv.text = "${musicState?.musicItems?.size} songs"
    }
}