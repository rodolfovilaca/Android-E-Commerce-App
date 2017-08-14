package com.example.rodolfo.testeappalpha

import ui.PageFragment
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by conra on 31/03/2017.
 */
class TabsFragmentPageAdapter(val fm :FragmentManager,private val context: Context): FragmentPagerAdapter(fm) {
    private val PAGE_COUNT = 3
    private val titles = listOf("Pratos","Em Alta","Favoritos")

    override fun getItem(position: Int): Fragment {
        val page: PageFragment = PageFragment()
        return page.newInstance(position + 1)
    }

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}