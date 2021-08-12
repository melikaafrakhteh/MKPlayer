package com.afrakhteh.musicplayer.views.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Constants
import com.afrakhteh.musicplayer.databinding.ActivityMainBinding
import com.afrakhteh.musicplayer.model.repository.MusicRepositoryImpl
import com.afrakhteh.musicplayer.viewModel.MainActivityViewModel
import com.afrakhteh.musicplayer.viewModel.MainViewModelFactory
import com.afrakhteh.musicplayer.views.adapter.ViewPagerAdapter
import com.afrakhteh.musicplayer.views.base.BaseActivity
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener


@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context
    private lateinit var viewModel: MainActivityViewModel

    private var currentFragment: Int = 10

    companion object {
        const val ALL_MUSIC_FRAGMENT = 0
        const val LIKED_FRAGMENT = 1
        const val RECENTLY_FRAGMENT = 2
        const val PLAY_LIST_FRAGMENT = 3
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        removeStatusBar()
        bindingSetUp()

        val repostory = MusicRepositoryImpl(this)
        val factory = MainViewModelFactory(repostory)
        viewModel = ViewModelProvider(this, factory)[MainActivityViewModel::class.java]

        defaultPage()
        setUpToolBar()
        buttonClick()
        setUpViewPager()
        requestStoragePermission()

    }

    //already granted
    private fun hasReadStorage() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestStoragePermission() {
        if (!hasReadStorage()) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.REQUEST_READ_STORAGE_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.REQUEST_READ_STORAGE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                permissionGranted()
            } else {
                Toast.makeText(context, "deny", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun permissionGranted() {

        viewModel.musicState.observe(this, Observer {
            Log.d("musicss", it.musicItems.toString())
            Toast.makeText(context, it.musicItems.toString(), Toast.LENGTH_LONG).show()
        })
        viewModel.fetchAllMusic()
    }

    private fun buttonClick() {
        binding.homeAllMusicLinear.setOnClickListener {
            binding.homeViewPager.currentItem = 0
            deSelectChooseLinearFragmentItem(currentFragment)
            currentFragment = ALL_MUSIC_FRAGMENT
            selectChooseLinearFragmentItem(currentFragment)
        }
        binding.homeLikedLinear.setOnClickListener {
            binding.homeViewPager.currentItem = 1
            deSelectChooseLinearFragmentItem(currentFragment)
            currentFragment = LIKED_FRAGMENT
            selectChooseLinearFragmentItem(currentFragment)
        }
        binding.homeRecentlyLinear.setOnClickListener {
            binding.homeViewPager.currentItem = 2
            deSelectChooseLinearFragmentItem(currentFragment)
            currentFragment = RECENTLY_FRAGMENT
            selectChooseLinearFragmentItem(currentFragment)
        }
        binding.homePlayListLinear.setOnClickListener {
            binding.homeViewPager.currentItem = 3
            deSelectChooseLinearFragmentItem(currentFragment)
            currentFragment = PLAY_LIST_FRAGMENT
            selectChooseLinearFragmentItem(currentFragment)
        }
    }

    private fun defaultPage() {
        currentFragment = ALL_MUSIC_FRAGMENT
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun selectChooseLinearFragmentItem(item: Int) {
        when (item) {
            ALL_MUSIC_FRAGMENT -> {
                binding.homeAllMusicTextTv.setTextColor(getColor(R.color.white))
                binding.homeAllMusicCircleIv.visibility = View.VISIBLE
            }
            LIKED_FRAGMENT -> {
                binding.homeLikedTextTv.setTextColor(getColor(R.color.white))
                binding.homeLikedCircleIv.visibility = View.VISIBLE
            }
            RECENTLY_FRAGMENT -> {
                binding.homeRecentlyTextTv.setTextColor(getColor(R.color.white))
                binding.homeRecentlycCircleIv.visibility = View.VISIBLE
            }
            PLAY_LIST_FRAGMENT -> {
                binding.homePlayListTextTv.setTextColor(getColor(R.color.white))
                binding.homePlayListCircleIv.visibility = View.VISIBLE
            }
        }
    }

    private fun deSelectChooseLinearFragmentItem(item: Int) {
        when (item) {
            ALL_MUSIC_FRAGMENT -> {
                binding.homeAllMusicTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeAllMusicCircleIv.visibility = View.GONE
            }
            LIKED_FRAGMENT -> {
                binding.homeLikedTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeLikedCircleIv.visibility = View.GONE
            }
            RECENTLY_FRAGMENT -> {
                binding.homeRecentlyTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeRecentlycCircleIv.visibility = View.GONE
            }
            PLAY_LIST_FRAGMENT -> {
                binding.homePlayListTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homePlayListCircleIv.visibility = View.GONE
            }
        }
    }

    private fun setUpViewPager() {
        binding.homeViewPager.adapter = ViewPagerAdapter(this, Constants.FRAGMENTS_NUMBER)
        binding.homeScrollView.isFillViewport = true
    }

    private fun setUpToolBar() {
        fixHomeCoverHeightSize()

        binding.homeAppBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val offsetAlpha: Float = -(appBarLayout.y / appBarLayout.totalScrollRange)
            binding.homeCustomToolBar.toolbar.alpha = 0 + offsetAlpha * +1
            binding.homeCustomToolBar.homeToolbarTv.alpha = 0 + offsetAlpha * +1

        })
    }

    private fun fixHomeCoverHeightSize() {

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var width: Int = size.x
        val height: Int = size.y

        binding.homeCoverImageIv.layoutParams.height = height / 2
        binding.homeCoverImageIv.layoutParams.width = width

    }

    private fun removeStatusBar() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    private fun bindingSetUp() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}