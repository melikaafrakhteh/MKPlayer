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
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.viewModel.PlayListMusicsViewModel
import com.afrakhteh.musicplayer.views.main.adapters.playListWithMusics.PlayListWithMusicsAdapter
import com.afrakhteh.musicplayer.views.main.customs.DeleteItemsDialog
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
    ): View {
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
        binding.playListMusicRecycler.apply {
            adapter = playListAdapter
            setItemViewCacheSize(10)
            isDrawingCacheEnabled = true
        }
        binding.playListMusicTitleTv.text = name
        viewModel.allPlayListMusic.observe(viewLifecycleOwner, ::renderPlayerList)
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchMusicOfThisPlayList(requireNotNull(position))
    }

    private fun onRemoveItemClick(musicPosition: Int) {
        DeleteItemsDialog {
            viewModel.deleteOneMusicFromPlayList(
                    playListAdapter.currentList[musicPosition],
                    requireNotNull(position)
            )
            Toast.makeText(context,
                    getString(R.string.remove_successfully_toast), Toast.LENGTH_LONG).show()
            viewModel.fetchMusicOfThisPlayList(requireNotNull(position))
        }.show(requireActivity().supportFragmentManager, "delete One Music")
    }


    private fun renderPlayerList(list: List<MusicEntity>?) {
        playListAdapter.submitList(list)
        binding.playListMusicNumberTv.text = if (list?.size == null) "0 song" else "${list.size} songs"
    }

    private fun onItemClick(position: Int) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java).apply {
            putParcelableArrayListExtra(
                    Strings.AUDIO_All_MUSIC_LIST_KEY,
                    ArrayList(audioPrePareToPlayList())
            )
            putExtra(Strings.AUDIO_ACTIVE_POSITION__KEY, position)
        }
        startActivity(intent)
    }

    private fun audioPrePareToPlayList(): List<AudioPrePareToPlay>? {
        return viewModel.allPlayListMusic.value?.map { musicEntity ->
            AudioPrePareToPlay(
                    id = musicEntity.index,
                    path = musicEntity.path,
                    musicName = musicEntity.name ?: "",
                    musicArtist = musicEntity.artist ?: ""
            )
        }
    }


}