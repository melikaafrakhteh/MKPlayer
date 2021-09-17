package com.afrakhteh.musicplayer.views.mainActivity.adapters.viewPager

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.afrakhteh.musicplayer.views.mainActivity.fragments.AllMusicFragment
import com.afrakhteh.musicplayer.views.mainActivity.fragments.LikedFragment
import com.afrakhteh.musicplayer.views.mainActivity.fragments.PlayListFragment
import com.afrakhteh.musicplayer.views.mainActivity.fragments.RecentlyFragment

class ViewPagerAdapter(activity: AppCompatActivity, private val fragmentNumber: Int) :
    FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return fragmentNumber
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {

            0 -> AllMusicFragment()

            1 -> LikedFragment()

            2 -> RecentlyFragment()

            3 -> PlayListFragment()

            else -> AllMusicFragment()

        }
    }
}