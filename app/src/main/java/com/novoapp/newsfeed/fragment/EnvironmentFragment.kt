package com.novoapp.newsfeed.fragment

import android.os.Bundle
import androidx.loader.content.Loader
import android.util.Log
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.News
import com.novoapp.newsfeed.NewsLoader
import com.novoapp.newsfeed.NewsPreferences

/**
 * The EnvironmentFragment is a [BaseArticlesFragment] subclass that
 * reuses methods of the parent class by passing the specific type of article to be fetched.
 */
class EnvironmentFragment : BaseArticlesFragment() {

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<News>> {
        val environmentUrl = NewsPreferences.getPreferredUrl(context, getString(R.string.environment))
        Log.e(LOG_TAG, environmentUrl)

        // Create a new loader for the given URL
        return NewsLoader(activity, environmentUrl)
    }

    companion object {

        private val LOG_TAG = EnvironmentFragment::class.java!!.getName()
    }
}
