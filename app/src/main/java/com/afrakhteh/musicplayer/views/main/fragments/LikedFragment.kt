package com.afrakhteh.musicplayer.views.main.fragments

import android.annotation.SuppressLint
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
import com.afrakhteh.musicplayer.databinding.FragmentLikedBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.viewModel.LikedViewModel
import com.afrakhteh.musicplayer.views.main.adapters.liked.LikedAdapter
import com.afrakhteh.musicplayer.views.main.customs.DeleteItemsDialog
import com.afrakhteh.musicplayer.views.main.state.MusicState
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import javax.inject.Inject


class LikedFragment : Fragment() {

    private lateinit var binding: FragmentLikedBinding

    @Inject
    lateinit var provideFragment: ViewModelProvider.Factory
    private val viewModel: LikedViewModel by viewModels { provideFragment }

    private lateinit var likedAdapter: LikedAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLikedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllMusicLikedList()
    }

    override fun onAttach(activity: Activity) {
        ViewModelComponentBuilder.getInstance().inject(this)
        super.onAttach(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        likedAdapter = LikedAdapter(::onItemClicked,
                ::removeFromLikedList,
                ::deleteMusic,
                viewModel.repository
        )
        binding.likeFragmentRecycler.apply {
            adapter = likedAdapter
            setItemViewCacheSize(10)
            isDrawingCacheEnabled = true
        }
        viewModel.state.observe(viewLifecycleOwner, ::renderList)
        viewModel.removeMusicFromList.observe(viewLifecycleOwner, ::checkIfMusicRemovedFromList)
    }

    private fun checkIfMusicRemovedFromList(isRemoved: Boolean?) {
        if (isRemoved == true) {
            viewModel.getAllMusicLikedList()
        }
    }

    private fun deleteMusic(musicPosition: Int) {
        DeleteItemsDialog {
            viewModel.deleteMusic(likedAdapter.currentList[musicPosition])
            Toast.makeText(context, R.string.delete_item_message, Toast.LENGTH_LONG).show()
        }.show(requireActivity().supportFragmentManager, "delete liked")
    }

    private fun removeFromLikedList(path: String) {
        viewModel.removeMusicFromLikedList(path)
        Toast.makeText(context, R.string.remove_music_Liked, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("SetTextI18n")
    private fun renderList(musicState: MusicState?) {
        likedAdapter.submitList(musicState?.musicItems)
        val size = musicState?.musicItems?.size
        binding.likeFragmentNumberTv.text = "$size song" + if (size == 0 || size == 1) "" else "s"
    }

    private fun onItemClicked(position: Int) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java).apply {
            putExtra(Strings.AUDIO_ACTIVE_POSITION__KEY, position)
            putParcelableArrayListExtra(Strings.AUDIO_All_MUSIC_LIST_KEY, ArrayList(audioPrePareToPlayList()))
        }
        startActivity(intent)
    }

    private fun audioPrePareToPlayList(): List<AudioPrePareToPlay>? {
        return viewModel.state.value?.musicItems?.map { musicEntity ->
            AudioPrePareToPlay(
                    id = musicEntity.index,
                    path = musicEntity.path,
                    musicName = musicEntity.name ?: "",
                    musicArtist = musicEntity.artist ?: ""
            )
        }
    }
}