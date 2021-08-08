package com.afrakhteh.musicplayer.views.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.afrakhteh.musicplayer.views.fragments.AllMusicFragment
import com.afrakhteh.musicplayer.views.fragments.LikedFragment
import com.afrakhteh.musicplayer.views.fragments.PlayListFragment
import com.afrakhteh.musicplayer.views.fragments.RecentlyFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
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

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "All"
            1 -> return "Liked"
            2 -> return "Recently"
            3 -> return "PlayList"
        }
        return null
    }
}