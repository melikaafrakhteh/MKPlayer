package com.afrakhteh.musicplayer.views.mainActivity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Numerals
import com.afrakhteh.musicplayer.constant.Strings
import com.afrakhteh.musicplayer.databinding.ActivityMainBinding
import com.afrakhteh.musicplayer.model.entity.AudioPrePareToPlay
import com.afrakhteh.musicplayer.model.entity.MusicEntity
import com.afrakhteh.musicplayer.util.ListUtil
import com.afrakhteh.musicplayer.util.getScreenSize
import com.afrakhteh.musicplayer.views.mainActivity.adapters.viewPager.ViewPagerAdapter
import com.afrakhteh.musicplayer.views.mainActivity.interfaces.MusicPlayer
import com.afrakhteh.musicplayer.views.mainActivity.interfaces.PermissionController
import com.afrakhteh.musicplayer.views.services.AudioPlayerService


@Suppress("IMPLICIT_CAST_TO_ANY")
class MainActivity : AppCompatActivity(), PermissionController, MusicPlayer {

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context

    private var permissionRequestCallBack: (Boolean) -> Unit = {}

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
    }

    private fun setUpUI() {
        //remove status bar
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.systemUiVisibility = uiOptions

        //cover
        binding.homeCoverImageIv.layoutParams.height = this.getScreenSize().x
        binding.homeCoverImageIv.layoutParams.width = this.getScreenSize().x

        //viewPager
        binding.homeViewPager.adapter = ViewPagerAdapter(this, Numerals.FRAGMENTS_NUMBER)
        binding.homeViewPager.registerOnPageChangeCallback(ViewPagerCallBack())

        buttonClick()
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
                permissionRequestCallBack.invoke(true)
            } else {
                Toast.makeText(context, "deny", Toast.LENGTH_LONG).show()
                permissionRequestCallBack.invoke(false)
            }
        }

    }

    override fun requestPermission() {
        requestStoragePermission()
    }

    override fun setOnPermissionRequestCallBack(callBack: (Boolean) -> Unit) {
        permissionRequestCallBack = callBack
    }

    override fun hasPermission(): Boolean {
        return hasReadStoragePermission()
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
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun likedViews(view: View) {
        binding.homeViewPager.currentItem = 1
        deSelectChooseLinearFragmentItem(currentFragment)
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun recentlyViews(view: View) {
        binding.homeViewPager.currentItem = 2
        deSelectChooseLinearFragmentItem(currentFragment)
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun playListViews(view: View) {
        binding.homeViewPager.currentItem = 3
        deSelectChooseLinearFragmentItem(currentFragment)
        selectChooseLinearFragmentItem(currentFragment)
    }

    private fun selectChooseLinearFragmentItem(item: Int) {
        selectTab(getTabTextView(item), getTabCircle(item))
    }

    private fun deSelectChooseLinearFragmentItem(item: Int) {
        deSelectTab(getTabTextView(item), getTabCircle(item))
    }

    private fun selectTab(tv: TextView, circle: ImageView) {
        tv.setTextColor(getColor(R.color.white))
        circle.visibility = View.VISIBLE
    }

    private fun deSelectTab(tv: TextView, circle: ImageView) {
        tv.setTextColor(getColor(R.color.white_disable))
        circle.visibility = View.GONE
    }

    private fun getTabTextView(position: Int): TextView {
        return when (position) {
            ALL_MUSIC_FRAGMENT -> binding.homeAllMusicTextTv
            LIKED_FRAGMENT -> binding.homeLikedTextTv
            RECENTLY_FRAGMENT -> binding.homeRecentlyTextTv
            else -> binding.homePlayListTextTv
        }
    }

    private fun getTabCircle(position: Int): ImageView {
        return when (position) {
            ALL_MUSIC_FRAGMENT -> binding.homeAllMusicCircleIv
            LIKED_FRAGMENT -> binding.homeLikedCircleIv
            RECENTLY_FRAGMENT -> binding.homeRecentlyCircleIv
            else -> binding.homePlayListCircleIv
        }
    }

    private inner class ViewPagerCallBack : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

            deSelectTab(getTabTextView(currentFragment), getTabCircle(currentFragment))
            currentFragment = position
            selectTab(getTabTextView(currentFragment), getTabCircle(currentFragment))
        }
    }

    override fun play(data: MusicEntity, list: List<AudioPrePareToPlay>) {
        startPlayerService(data)
        ListUtil.setList(list)
    }

    override fun pause() {}

    override fun stop() {}

    private fun startPlayerService(data: MusicEntity) {
        val serviceIntent = Intent(this, AudioPlayerService::class.java)
                .putExtra(Strings.AUDIO_ID_KEY, data.index)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
}


