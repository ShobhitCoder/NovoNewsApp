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
 * The SocietyFragment is a [BaseArticlesFragment] subclass that
 * reuses methods of the parent class by passing the specific type of article to be fetched.
 */
class SocietyFragment : BaseArticlesFragment() {

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<News>> {
        val societyUrl = NewsPreferences.getPreferredUrl(context, getString(R.string.society))
        Log.e(LOG_TAG, societyUrl)

        // Create a new loader for the given URL
        return NewsLoader(activity, societyUrl)
    }

    companion object {

        private val LOG_TAG = SocietyFragment::class.simpleName}
}
