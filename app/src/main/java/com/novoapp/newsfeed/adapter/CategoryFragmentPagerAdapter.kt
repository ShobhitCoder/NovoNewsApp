package com.novoapp.newsfeed.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.fragment.*
import com.novoapp.newsfeed.utils.Constants

/**
 * Provides the appropriate [Fragment] for a view pager.
 */

class CategoryFragmentPagerAdapter
/**
 * Create a new [CategoryFragmentPagerAdapter] object.
 *
 * @param context is the context of the app
 * @param fm is the fragment manager that will keep each fragment's state in the adapter
 * across swipes.
 */
(
        /** Context of the app  */
        private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    /**
     * Return the [Fragment] that should be displayed for the given page number.
     */
    override fun getItem(position: Int): Fragment {
        when (position) {
            Constants.HOME -> return HomeFragment()
            Constants.WORLD -> return WorldFragment()
            Constants.SCIENCE -> return ScienceFragment()
            Constants.SPORT -> return SportFragment()
            Constants.ENVIRONMENT -> return EnvironmentFragment()
            Constants.SOCIETY -> return SocietyFragment()
            Constants.FASHION -> return FashionFragment()
            Constants.BUSINESS -> return BusinessFragment()
            Constants.CULTURE -> return CultureFragment()
            else -> HomeFragment()
        }
        return HomeFragment()
    }

    /**
     * Return the total number of pages.
     */
    override fun getCount(): Int {
        return 9
    }

    /**
     * Return page title of the tap
     */
    override fun getPageTitle(position: Int): CharSequence? {
        val titleResId: Int
        when (position) {
            Constants.HOME -> titleResId = R.string.ic_title_home
            Constants.WORLD -> titleResId = R.string.ic_title_world
            Constants.SCIENCE -> titleResId = R.string.ic_title_science
            Constants.SPORT -> titleResId = R.string.ic_title_sport
            Constants.ENVIRONMENT -> titleResId = R.string.ic_title_environment
            Constants.SOCIETY -> titleResId = R.string.ic_title_society
            Constants.FASHION -> titleResId = R.string.ic_title_fashion
            Constants.BUSINESS -> titleResId = R.string.ic_title_business
            else -> titleResId = R.string.ic_title_culture
        }
        return mContext.getString(titleResId)
    }
}