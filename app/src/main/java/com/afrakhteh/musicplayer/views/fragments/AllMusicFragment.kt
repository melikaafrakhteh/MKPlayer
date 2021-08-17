package com.afrakhteh.musicplayer.views.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrakhteh.musicplayer.databinding.FragmentAllMusicBinding
import com.afrakhteh.musicplayer.di.builders.RepositoryComponentBuilder
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.viewModel.MainActivityViewModel
import com.afrakhteh.musicplayer.views.adapter.AllMusicAdapter
import com.afrakhteh.musicplayer.views.state.MusicState
import javax.inject.Inject

class AllMusicFragment : Fragment() {

    private lateinit var binding: FragmentAllMusicBinding
    @Inject
    lateinit var viewModel: MainActivityViewModel

    private lateinit var musicAdapter: AllMusicAdapter
    private var musicList: List<MusicEntity> = listOf()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllMusicBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        RepositoryComponentBuilder.getInstance().inject(this)
        super.onAttach(context)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        musicAdapter = AllMusicAdapter(this::onMusicItemClicked)
        binding.allFragmentRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = musicAdapter
        }
        viewModel.state.observe(requireActivity(), this::renderState)
        viewModel.fetchAllMusic()
    }

    private fun onMusicItemClicked(data: MusicEntity) {
        Toast.makeText(requireContext(), data.name, Toast.LENGTH_LONG).show()
    }

    private fun renderState(state: MusicState) {
        state.message?.ifNotHandled {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        musicAdapter.submitList(state.musicItems)
        var number = state.musicItems.size
        binding.allFragmentNumberTv.text = "$number songs"


    }

    fun onPermissionGranted(permission: Boolean){
        if (permission){
            viewModel.state.observe(requireActivity(), this::renderState)
            viewModel.fetchAllMusic()
        }
    }
}