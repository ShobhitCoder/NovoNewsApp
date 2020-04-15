package com.novoapp.newsfeed.fragment

import android.os.Bundle
import androidx.loader.content.Loader
import android.util.Log
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.News
import com.novoapp.newsfeed.NewsLoader
import com.novoapp.newsfeed.NewsPreferences


/**
 * The BusinessFragment is a [BaseArticlesFragment] subclass that
 * reuses methods of the parent class by passing the specific type of article to be fetched.
 */
class BusinessFragment : BaseArticlesFragment() {

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<News>> {
        val businessUrl = NewsPreferences.getPreferredUrl(context, getString(R.string.business))
        Log.e(LOG_TAG, businessUrl)

        // Create a new loader for the given URL
        return NewsLoader(activity, businessUrl)
    }

    companion object {

        private val LOG_TAG = BusinessFragment::class.java!!.getName()
    }
}
