package com.novoapp.newsfeed.fragment

import android.os.Bundle
import androidx.loader.content.Loader
import android.util.Log
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.News
import com.novoapp.newsfeed.NewsLoader
import com.novoapp.newsfeed.NewsPreferences
import com.novoapp.newsfeed.fragment.BaseArticlesFragment


/**
 * The ScienceFragment is a [BaseArticlesFragment] subclass that
 * reuses methods of the parent class by passing the specific type of article to be fetched.
 */
class ScienceFragment : BaseArticlesFragment() {

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<News>> {
        val scienceUrl = NewsPreferences.getPreferredUrl(context, getString(R.string.science))
        Log.e(LOG_TAG, scienceUrl)

        // Create a new loader for the given URL
        return NewsLoader(activity, scienceUrl)
    }

    companion object {

        private val LOG_TAG = ScienceFragment::class.java!!.getName()
    }
}
