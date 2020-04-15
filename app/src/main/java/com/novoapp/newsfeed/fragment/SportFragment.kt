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
 * The SportFragment is a [BaseArticlesFragment] subclass that
 * reuses methods of the parent class by passing the specific type of article to be fetched.
 */
class SportFragment : BaseArticlesFragment() {

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<News>> {
        val sportUrl = NewsPreferences.getPreferredUrl(context, getString(R.string.sport))
        Log.e(LOG_TAG, sportUrl)

        // Create a new loader for the given URL
        return NewsLoader(activity, sportUrl)
    }

    companion object {

        private val LOG_TAG = SportFragment::class.java!!.getName()
    }
}
