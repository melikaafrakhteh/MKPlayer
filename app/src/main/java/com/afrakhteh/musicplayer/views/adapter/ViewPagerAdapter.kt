package com.afrakhteh.musicplayer.views.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.afrakhteh.musicplayer.views.fragments.AllMusicFragment
import com.afrakhteh.musicplayer.views.fragments.LikedFragment
import com.afrakhteh.musicplayer.views.fragments.PlayListFragment
import com.afrakhteh.musicplayer.views.fragments.RecentlyFragment

class ViewPagerAdapter(activity: AppCompatActivity, val fragmentNumber: Int) : FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return fragmentNumber
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return AllMusicFragment()
            }
            1 -> {
                return LikedFragment()
            }
            2 -> {
                return RecentlyFragment()
            }
            3 -> {
                return PlayListFragment()
            }
        }
        return AllMusicFragment()
    }
}
