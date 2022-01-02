package com.afrakhteh.musicplayer.views.main.fragments

import android.annotation.SuppressLint
import android.app.Activity
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
import com.afrakhteh.musicplayer.databinding.FragmentPlayListBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.util.SingleEvent
import com.afrakhteh.musicplayer.viewModel.PlayListViewModel
import com.afrakhteh.musicplayer.views.main.adapters.playList.PlayListAdapter
import com.afrakhteh.musicplayer.views.main.customs.DeleteItemsDialog
import com.afrakhteh.musicplayer.views.main.state.PlayListState
import javax.inject.Inject

class PlayListFragment : Fragment() {
    private lateinit var binding: FragmentPlayListBinding

    @Inject
    lateinit var provideFragment: ViewModelProvider.Factory
    private val viewModel: PlayListViewModel by viewModels { provideFragment }

    private lateinit var playListAdapter: PlayListAdapter


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(activity: Activity) {
        ViewModelComponentBuilder.getInstance().inject(this)
        super.onAttach(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchAllPlayList()
        playListAdapter = PlayListAdapter(::onItemClick, ::onMenuItemClick, viewModel.repository)
        binding.playListFragmentRecycler.adapter = playListAdapter
        viewModel.state.observe(viewLifecycleOwner, ::renderPlayLists)
        viewModel.deletePlayList.observe(viewLifecycleOwner, ::deletePlayList)

    }

    private fun deletePlayList(isDeleted: SingleEvent<Boolean>?) {
        isDeleted?.ifNotHandled {
            if (it) {
                viewModel.fetchAllPlayList()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchAllPlayList()
    }

    private fun onMenuItemClick(position: Int) {
        DeleteItemsDialog {
            viewModel.deleteSelectedPlayList(position)
            Toast.makeText(context,
                    getString(R.string.delete_playList_toast), Toast.LENGTH_LONG).show()
        }
                .show(requireActivity().supportFragmentManager, "delete playlist")
    }

    private fun onItemClick(position: Int) {
        val fragment = PlayListMusicsFragment()
        val bundle = Bundle().apply {
            putInt(Strings.PLAY_LIST_POSITION_KEY,
                    requireNotNull(playListAdapter.currentList[position].id))
            putString(Strings.PLAY_LIST_NAME_KEY,
                    playListAdapter.currentList[position].title
            )
        }
        fragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.playListContainer, fragment)
                .addToBackStack(null)
                .setReorderingAllowed(true) // optimizing operations within and across transactions
                .commit()
    }

    @SuppressLint("SetTextI18n")
    private fun renderPlayLists(playListState: PlayListState) {
        playListState.message?.ifNotHandled {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
        playListAdapter.submitList(ArrayList(playListState.playLists))
        val playListSize = playListState.playLists.size
        binding.playListFragmentNumberTv.text = "$playListSize playList" +
                if (playListSize == 1 || playListSize == 0) "" else "s"
    }
}