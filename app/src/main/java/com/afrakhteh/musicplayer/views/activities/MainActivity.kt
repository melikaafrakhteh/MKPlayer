package com.afrakhteh.musicplayer.views.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.afrakhteh.musicplayer.R
import com.afrakhteh.musicplayer.databinding.ActivityMainBinding
import com.afrakhteh.musicplayer.views.adapter.ViewPagerAdapter
import com.afrakhteh.musicplayer.views.base.BaseActivity
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

        bindingSetUp()
        removeStatusBar()
        setCoverImageView()
        setUpToolBar()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        binding.homeViewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        binding.homeTabLayout.setupWithViewPager(binding.homeViewPager)
        binding.homeScrollView.isFillViewport = true
    }

    private fun setUpToolBar() {
        binding.homeAppBarLayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->

            val scroll = -(verticalOffset)
           // Toast.makeText(context, scroll.toString(), Toast.LENGTH_SHORT).show()
            if (scroll >= 600) {
                binding.mainCustomToolbar.toolbar.visibility = View.VISIBLE
            } else {
                binding.mainCustomToolbar.toolbar.visibility = View.GONE
            }
        })

    }

    private fun removeStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun setCoverImageView() {
        Glide.with(context).load(R.drawable.music).into(binding.homeCoverImageIv)
    }

    private fun bindingSetUp() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}