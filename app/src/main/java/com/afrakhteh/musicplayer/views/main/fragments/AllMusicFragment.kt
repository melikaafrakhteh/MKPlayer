package com.afrakhteh.musicplayer.views.main.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Lists
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.databinding.FragmentAllMusicBinding
import com.afrakhteh.musicplayer.di.builders.ViewModelComponentBuilder
import com.afrakhteh.musicplayer.model.entity.audio.AllPlayListEntity
import com.afrakhteh.musicplayer.model.entity.audio.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.audio.MusicEntity
import com.afrakhteh.musicplayer.viewModel.AllMusicViewModel
import com.afrakhteh.musicplayer.views.main.adapters.allMusic.AllMusicAdapter
import com.afrakhteh.musicplayer.views.main.adapters.allMusic.dialog.PlayListDialogAdapter
import com.afrakhteh.musicplayer.views.main.customs.CreatePlayListDialog
import com.afrakhteh.musicplayer.views.main.customs.DeleteItemsDialog
import com.afrakhteh.musicplayer.views.main.interfaces.MainCoverController
import com.afrakhteh.musicplayer.views.main.interfaces.OnCreateDialogPlayListNameSelectedListener
import com.afrakhteh.musicplayer.views.main.interfaces.OnDeleteItemSelectedListener
import com.afrakhteh.musicplayer.views.main.interfaces.PermissionController
import com.afrakhteh.musicplayer.views.main.state.MusicState
import com.afrakhteh.musicplayer.views.musicPlayer.PlayerActivity
import com.afrakhteh.musicplayer.views.util.measureContentWidth
import kotlinx.coroutines.launch
import javax.inject.Inject


class AllMusicFragment : Fragment() {

    private lateinit var binding: FragmentAllMusicBinding


    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private val viewModel: AllMusicViewModel by viewModels { providerFactory }

    private lateinit var musicAdapter: AllMusicAdapter
    private lateinit var dialogAdapter: PlayListDialogAdapter

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

    override fun onResume() {
        super.onResume()
        viewModel.fetchAllPlayListTitle()
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
        val onDeleteItem = object : OnDeleteItemSelectedListener {
            override fun isItemDeleted() {
                viewModel.deleteMusicFromList(item)
                Toast.makeText(context, R.string.delete_item_message, Toast.LENGTH_LONG).show()
            }
        }
        DeleteItemsDialog(onDeleteItem).show(requireActivity().supportFragmentManager, "delete")
    }

    private fun onMusicAddToPlayListClicked(musicPosition: Int, view: View) {
        showChoosePlayListDialog(musicPosition, view)
    }

    private fun showChoosePlayListDialog(musicPosition: Int, view: View) {
        val menuListAdapter = ArrayAdapter(
                requireContext(),
                R.layout.popup_menu_item,
                Lists.POPUP_MENU_ITEMS_CHOOSE_PLAY_LIST
        )
        setUpPopUpMenu(menuListAdapter, view).apply {
            setOnItemClickListener { _, _, position, _ ->
                when (position) {
                    0 -> {
                        openChoosePlayLists(musicPosition, view)
                        this.dismiss()
                    }
                    1 -> {
                        showCreateNewPlayListDialog()
                        this.dismiss()
                    }
                }
            }
        }.show()
    }

    private fun setUpPopUpMenu(menuListAdapter: ListAdapter, view: View): ListPopupWindow {
        return ListPopupWindow(requireContext())
                .apply {
                    anchorView = view
                    isModal = true
                    setAdapter(menuListAdapter)
                    width = menuListAdapter.measureContentWidth(requireContext())
                }
    }

    private fun openChoosePlayLists(musicId: Int, view: View) {
        viewModel.fetchAllPlayListTitle()
        dialogAdapter = PlayListDialogAdapter(::onPlayListDialogClick, musicId)
        val playLists = ArrayAdapter(requireContext(),
                R.layout.popup_menu_item,
                viewModel.playListTitle.value as MutableList<String>)

        if (playLists.isEmpty) {
            Toast.makeText(
                    requireContext(),
                    getString(R.string.no_playlist_toast),
                    Toast.LENGTH_LONG).show()
        }
        setUpPopUpMenu(playLists, view).apply {
            setOnItemClickListener { _, _, position, _ ->
                onPlayListDialogClick(position = position + 1, musicPosition = musicId)
                dismiss()
            }

        }.show()

    }

    private fun onPlayListDialogClick(position: Int, musicPosition: Int) {
        lifecycleScope.launch {
            viewModel.addMusicToPlayList(
                    musicAdapter.currentList[musicPosition], position
            )
        }
        Toast.makeText(context,
                getString(R.string.added_playList_toast), Toast.LENGTH_LONG).show()
    }

    private fun showCreateNewPlayListDialog() {
        val onNameSelect = object : OnCreateDialogPlayListNameSelectedListener {
            override fun onPlayListNameSelected(name: String) {
                lifecycleScope.launch {
                    viewModel.createANewPlayList(
                            AllPlayListEntity(title = name)
                    )
                    Toast.makeText(context,
                            getString(R.string.created_playList_toast), Toast.LENGTH_LONG).show()
                    //openChoosePlayLists(playListPosition)
                }
            }
        }
        CreatePlayListDialog(onNameSelect).show(requireActivity().supportFragmentManager, "create")
    }

    private fun onMusicItemClicked(position: Int) {
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
        binding.allFragmentNumberTv.text = "$number song" +
                if (number == 0 || number == 1)"" else "s"

        loadCoverImage(
                if (state.musicItems.isEmpty()) null
                else state.musicItems[0].path
        )

    }

    private fun loadCoverImage(path: String?) {
        if (path == null) {
            (requireActivity() as MainCoverController).setCoverImage(null)
            return
        }
        lifecycleScope.launch {
            viewModel.repository.getMusicArtPicture(path).let {
                (requireActivity() as MainCoverController).setCoverImage(it)
            }
        }
    }

    private fun onPermissionGranted(permission: Boolean) {
        if (permission) {
            notifyRecentlyIfPermissionIsGranted(permission)
            viewModel.fetchAllMusic()
        } else {
            Toast.makeText(requireContext(), getString(R.string.deny_message), Toast.LENGTH_LONG)
                    .show()
        }
    }

    private fun notifyRecentlyIfPermissionIsGranted(permission: Boolean) {
        val bundle = Bundle().apply {
            putBoolean(Strings.CHECK_PERMISSION_NOTIFY_RECENTLY_KEY, permission)
        }
        RecentlyFragment().arguments = bundle
    }
}