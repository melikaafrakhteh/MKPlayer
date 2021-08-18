package com.afrakhteh.musicplayer.views.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.databinding.ActivityMainBinding
import com.afrakhteh.musicplayer.views.adapter.viewPager.ViewPagerAdapter
import com.afrakhteh.musicplayer.views.base.BaseActivity
import com.afrakhteh.musicplayer.views.fragments.AllMusicFragment
import com.google.android.material.appbar.AppBarLayout


@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context

    private var currentFragment: Int = 10

    companion object {
        const val ALL_MUSIC_FRAGMENT = 0
        const val LIKED_FRAGMENT = 1
        const val RECENTLY_FRAGMENT = 2
        const val PLAY_LIST_FRAGMENT = 3
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        currentFragment = ALL_MUSIC_FRAGMENT
        selectChooseLinearFragmentItem(currentFragment)
        setUpUI()
        requestStoragePermission()

    }

    private fun setUpUI() {
        //remove status bar
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions

        fixHomeCoverHeightSize()

        binding.homeAppBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener(::setToolBarCollapsingAlpha))

        //viewPager
        binding.homeViewPager.adapter = ViewPagerAdapter(this, Numerals.FRAGMENTS_NUMBER)
     //   binding.homeScrollView.isFillViewport = true

        buttonClick()
    }

    private fun setToolBarCollapsingAlpha(appBarLayout: AppBarLayout, verticalOffSet: Int) {
        val offsetAlpha: Float = -(appBarLayout.y / appBarLayout.totalScrollRange)
        binding.homeCustomToolBar.toolbar.alpha = 0 + offsetAlpha * +1
        binding.homeCustomToolBar.homeToolbarTv.alpha = 0 + offsetAlpha * +1
    }

    //already granted
    private fun hasReadStoragePermission() = ContextCompat
            .checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED

    private fun requestStoragePermission() {
        if (!hasReadStoragePermission()) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Numerals.REQUEST_READ_STORAGE_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Numerals.REQUEST_READ_STORAGE_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                permissionGranted(true)
            } else {
                Toast.makeText(context, "deny", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun permissionGranted(isGranted: Boolean) {
        val activeFragment = supportFragmentManager.primaryNavigationFragment
        if (activeFragment is AllMusicFragment) {
            activeFragment.onPermissionGranted(isGranted)
        }
    }

    private fun buttonClick() {
        binding.homeAllMusicLinear.setOnClickListener(this::allMusicViews)
        binding.homeLikedLinear.setOnClickListener(this::likedViews)
        binding.homeRecentlyLinear.setOnClickListener(this::recentlyViews)
        binding.homePlayListLinear.setOnClickListener(this::playListViews)
    }

    private fun allMusicViews(view: View) {
        binding.homeViewPager.currentItem = 0
        deSelectChooseLinearFragmentItem(currentFragment)
        currentFragment = ALL_MUSIC_FRAGMENT
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun likedViews(view: View) {
        binding.homeViewPager.currentItem = 1
        deSelectChooseLinearFragmentItem(currentFragment)
        currentFragment = LIKED_FRAGMENT
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun recentlyViews(view: View) {
        binding.homeViewPager.currentItem = 2
        deSelectChooseLinearFragmentItem(currentFragment)
        currentFragment = RECENTLY_FRAGMENT
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun playListViews(view: View) {
        binding.homeViewPager.currentItem = 3
        deSelectChooseLinearFragmentItem(currentFragment)
        currentFragment = PLAY_LIST_FRAGMENT
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun selectChooseLinearFragmentItem(item: Int) {
        when (item) {
            ALL_MUSIC_FRAGMENT -> {
                binding.homeAllMusicTextTv.setTextColor(getColor(R.color.white))
                binding.homeAllMusicTextTv.height = 34
                binding.homeAllMusicCircleIv.visibility = View.VISIBLE
            }
            LIKED_FRAGMENT -> {
                binding.homeLikedTextTv.setTextColor(getColor(R.color.white))
                binding.homeLikedTextTv.height = 34
                binding.homeLikedCircleIv.visibility = View.VISIBLE
            }
            RECENTLY_FRAGMENT -> {
                binding.homeRecentlyTextTv.setTextColor(getColor(R.color.white))
                binding.homeRecentlyTextTv.height = 34
                binding.homeRecentlycCircleIv.visibility = View.VISIBLE
            }
            PLAY_LIST_FRAGMENT -> {
                binding.homePlayListTextTv.setTextColor(getColor(R.color.white))
                binding.homePlayListTextTv.height = 34
                binding.homePlayListCircleIv.visibility = View.VISIBLE
            }
        }
    }

    private fun deSelectChooseLinearFragmentItem(item: Int) {
        when (item) {
            ALL_MUSIC_FRAGMENT -> {
                binding.homeAllMusicTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeAllMusicTextTv.height = 44
                binding.homeAllMusicCircleIv.visibility = View.GONE
            }
            LIKED_FRAGMENT -> {
                binding.homeLikedTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeLikedTextTv.height = 44
                binding.homeLikedCircleIv.visibility = View.GONE
            }
            RECENTLY_FRAGMENT -> {
                binding.homeRecentlyTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeRecentlyTextTv.height = 44
                binding.homeRecentlycCircleIv.visibility = View.GONE
            }
            PLAY_LIST_FRAGMENT -> {
                binding.homePlayListTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homePlayListTextTv.height = 44
                binding.homePlayListCircleIv.visibility = View.GONE
            }
        }
    }

    private fun fixHomeCoverHeightSize() {

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        var width: Int = size.x
        val height: Int = size.y

        binding.homeCoverImageIv.layoutParams.height = width
        binding.homeCoverImageIv.layoutParams.width = width

    }

}

