package com.novoapp.newsfeed.fragment

import android.os.Bundle
import android.util.Log
import androidx.loader.content.Loader
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.News
import com.novoapp.newsfeed.NewsLoader
import com.novoapp.newsfeed.NewsPreferences

/**
 * The CultureFragment is a [BaseArticlesFragment] subclass that
 * reuses methods of the parent class by passing the specific type of article to be fetched.
 */
class CultureFragment : BaseArticlesFragment() {

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<News>> {
        val cultureUrl = NewsPreferences.getPreferredUrl(context, getString(R.string.culture))
        Log.e(LOG_TAG, cultureUrl)

        // Create a new loader for the given URL
        return NewsLoader(activity, cultureUrl)
    }

    companion object {

        private val LOG_TAG = CultureFragment::class.java.getName()
    }
}
