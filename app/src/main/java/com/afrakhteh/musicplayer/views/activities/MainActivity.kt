package com.afrakhteh.musicplayer.views.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.constant.Constants
import com.afrakhteh.musicplayer.databinding.ActivityMainBinding
import com.afrakhteh.musicplayer.views.adapter.ViewPagerAdapter
import com.afrakhteh.musicplayer.views.base.BaseActivity
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context
    private lateinit var currentFragment: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        bindingSetUp()
        defaultPage()
        setUpToolBar()
        removeStatusBar()
        buttonClick()
       setUpViewPager()
    }

    private fun buttonClick() {
        binding.homeAllMusicLinear.setOnClickListener {
            allMusicClicked()
            binding.homeViewPager.currentItem = 0
        }
        binding.homeLikedLinear.setOnClickListener {
            likedClicked()
            binding.homeViewPager.currentItem = 1
        }
        binding.homeRecentlyLinear.setOnClickListener {
            recentlyClicked()
            binding.homeViewPager.currentItem = 2
        }
        binding.homePlayListLinear.setOnClickListener {
            playListClicked()
            binding.homeViewPager.currentItem = 3
        }
    }

    private fun defaultPage(){
        currentFragment = getString(R.string.all_title_tv)
        allMusicClicked()
    }
    private fun allMusicClicked(){
        deSelectChooseLinearFragmentItem(currentFragment)
        currentFragment = getString(R.string.all_title_tv)
        binding.homeAllMusicTextTv.setTextColor(getColor(R.color.white))
        binding.homeAllMusicCircleIv.visibility = View.VISIBLE
    }
    private fun likedClicked(){
        deSelectChooseLinearFragmentItem(currentFragment)
        currentFragment = getString(R.string.like_title_tv)
        binding.homeLikedTextTv.setTextColor(getColor(R.color.white))
        binding.homeLikedCircleIv.visibility = View.VISIBLE
    }
    private fun recentlyClicked(){
        deSelectChooseLinearFragmentItem(currentFragment)
        currentFragment = getString(R.string.recently_title_tv)
        binding.homeRecentlyTextTv.setTextColor(getColor(R.color.white))
        binding.homeRecentlycCircleIv.visibility = View.VISIBLE
    }
    private fun playListClicked(){
        deSelectChooseLinearFragmentItem(currentFragment)
        currentFragment = getString(R.string.playlist_title_tv)
        binding.homePlayListTextTv.setTextColor(getColor(R.color.white))
        binding.homePlayListCircleIv.visibility = View.VISIBLE
    }
    private fun deSelectChooseLinearFragmentItem(item: String){
        when(item){
            getString(R.string.all_title_tv) -> {
                binding.homeAllMusicTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeAllMusicCircleIv.visibility = View.GONE
            }
            getString(R.string.like_title_tv) -> {
                binding.homeLikedTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeLikedCircleIv.visibility = View.GONE
            }
            getString(R.string.recently_title_tv) -> {
                binding.homeRecentlyTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homeRecentlycCircleIv.visibility = View.GONE
            }
            getString(R.string.playlist_title_tv) -> {
                binding.homePlayListTextTv.setTextColor(getColor(R.color.white_disable))
                binding.homePlayListCircleIv.visibility = View.GONE
            }
        }
    }


    private fun setUpViewPager() {
        binding.homeViewPager.adapter = ViewPagerAdapter(this,Constants.FRAGMENTS_NUMBER)
        binding.homeScrollView.isFillViewport = true
    }


    private fun setUpToolBar() {
        binding.homeAppBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val scroll = -(verticalOffset)
           // Toast.makeText(context, scroll.toString(), Toast.LENGTH_SHORT).show()
            if (scroll >= 700) {
                binding.homeCustomToolBar.toolbar.visibility = View.VISIBLE
            } else {
                binding.homeCustomToolBar.toolbar.visibility = View.GONE
            }
        })

    }

    private fun removeStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun bindingSetUp() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}