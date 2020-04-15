package com.novoapp.newsfeed.fragment

import android.os.Bundle
import android.util.Log
import androidx.loader.content.Loader
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.News
import com.novoapp.newsfeed.NewsLoader
import com.novoapp.newsfeed.NewsPreferences
import com.novoapp.newsfeed.fragment.BaseArticlesFragment


/**
 * The FashionFragment is a [BaseArticlesFragment] subclass that
 * reuses methods of the parent class by passing the specific type of article to be fetched.
 */
class FashionFragment : BaseArticlesFragment() {

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<News>> {
        val fashionUrl = NewsPreferences.getPreferredUrl(context, getString(R.string.fashion))
        Log.e(LOG_TAG, fashionUrl)

        // Create a new loader for the given URL
        return NewsLoader(activity, fashionUrl)
    }

    companion object {

        private val LOG_TAG = FashionFragment::class.java!!.getName()
    }
}
