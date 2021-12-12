package com.afrakhteh.musicplayer.views.main.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.databinding.FragmentAllMusicBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.viewModel.AllMusicViewModel
import com.afrakhteh.musicplayer.views.main.adapters.allMusic.AllMusicAdapter
import com.afrakhteh.musicplayer.views.main.interfaces.PermissionController
import com.afrakhteh.musicplayer.views.main.state.MusicState
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import javax.inject.Inject


class AllMusicFragment : Fragment() {

    private lateinit var binding: FragmentAllMusicBinding

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private val viewModel: AllMusicViewModel by viewModels { providerFactory }

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
            setOnPermissionRequestCallBack(this@AllMusicFragment::onPermissionGranted)
            if (hasPermission()) {
                viewModel.fetchAllMusic()
            } else {
                requestPermission()
            }
        }
    }

    private fun initialiseView() {
        musicAdapter = AllMusicAdapter(
                this::onMusicItemClicked,
                this::onMusicAddToPlayListClicked,
                this::onMusicItemDeleteClicked,
                viewModel.repository)
        binding.allFragmentRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = musicAdapter
        }
    }

    private fun onMusicItemDeleteClicked(position: Int) {
        showRemoveMusicItemConfirmDialog(musicAdapter.currentList[position])
    }

    private fun showRemoveMusicItemConfirmDialog(item: MusicEntity) {
        AlertDialog.Builder(context)
                .setMessage(R.string.delete_dialog_content)
                .apply {
                    setPositiveButton(R.string.delete_dialog_yes) { _, _ ->
                        viewModel.deleteMusicFromList(item)
                        Toast.makeText(context, R.string.delete_item_message, Toast.LENGTH_LONG).show()
                    }
                    setNegativeButton(R.string.delete_dialog_no) { _, _ ->
                        // cancel
                    }
                }
                .show()
    }

    private fun onMusicAddToPlayListClicked(position: Int) {
        // add to playList
    }

    private fun onMusicItemClicked(position: Int) {
        val intent = Intent(requireActivity(), PlayerActivity::class.java).apply {
            putParcelableArrayListExtra(Strings.AUDIO_All_MUSIC_LIST_KEY, ArrayList(audioPrePareToPlayList()))
            putExtra(Strings.AUDIO_ACTIVE_POSITION__KEY, position)
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