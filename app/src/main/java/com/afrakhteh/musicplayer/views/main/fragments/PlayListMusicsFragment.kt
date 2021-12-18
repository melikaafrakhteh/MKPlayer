package com.afrakhteh.musicplayer.views.main.fragments

import android.app.Activity
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
import com.afrakhteh.musicplayer.databinding.FragmentPlayListMusicsBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.db.AllMusicsEntity
import com.afrakhteh.musicplayer.viewModel.PlayListMusicsViewModel
import com.afrakhteh.musicplayer.views.main.adapters.playListWithMusics.PlayListWithMusicsAdapter
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import javax.inject.Inject

class PlayListMusicsFragment : Fragment() {
    private lateinit var binding: FragmentPlayListMusicsBinding

    @Inject
    lateinit var provideFactory: ViewModelProvider.Factory
    private val viewModel: PlayListMusicsViewModel by viewModels { provideFactory }

    private var position: Int? = null
    private var name: String? = null
    private lateinit var playListAdapter: PlayListWithMusicsAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayListMusicsBinding.inflate(
                layoutInflater, container, false
        )
        return binding.root
    }

    override fun onAttach(activity: Activity) {
        ViewModelComponentBuilder.getInstance().inject(this)
        super.onAttach(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = arguments?.getInt(Strings.PLAY_LIST_POSITION_KEY)
        name = arguments?.getString(Strings.PLAY_LIST_NAME_KEY)
        viewModel.fetchMusicOfThisPlayList(requireNotNull(position))
        playListAdapter = PlayListWithMusicsAdapter(::onItemClick,
                ::onRemoveItemClick, viewModel.repository
        )
        binding.playListMusicRecycler.adapter = playListAdapter
        binding.playListMusicTitleTv.text = name
        binding.playListMusicBackTv.setOnClickListener(::backToPlayListClick)
        viewModel.allPlayListMusic.observe(viewLifecycleOwner, ::renderPlayerList)
    }

    private fun onRemoveItemClick(allMusicsEntity: AllMusicsEntity) {
        viewModel.deleteOneMusicFromPlayList(allMusicsEntity)
        Toast.makeText(context,
                getString(R.string.remove_successfully_toast), Toast.LENGTH_LONG).show()
        viewModel.fetchMusicOfThisPlayList(requireNotNull(position))
    }

    private fun backToPlayListClick(view: View?) {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun renderPlayerList(list: List<AllMusicsEntity>?) {
        playListAdapter.submitList(list)
        binding.playListMusicNumberTv.text = "${list?.size} songs"
    }

    private fun onItemClick(position: Int) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java).apply {
            putParcelableArrayListExtra(Strings.AUDIO_All_MUSIC_LIST_KEY, ArrayList(audioPrePareToPlayList()))
            putExtra(Strings.AUDIO_ACTIVE_POSITION__KEY, position)
        }
        startActivity(intent)
    }

    private fun audioPrePareToPlayList(): List<AudioPrePareToPlay>? {
        return viewModel.allPlayListMusic.value?.map { AllMusicEntity ->
            AudioPrePareToPlay(
                    id = AllMusicEntity.musicId,
                    path = AllMusicEntity.path,
                    musicName = AllMusicEntity.name,
                    musicArtist = AllMusicEntity.artist
            )
        }
    }
}