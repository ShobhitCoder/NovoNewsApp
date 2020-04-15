package com.novoapp.newsfeed

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import com.novoapp.newsfeed.utils.QueryUtils

/**
 * Loads a list of news by using an AsyncTask to perform the network request to the given URL.
 */
class NewsLoader
/**
 * Constructs a new [NewsLoader].
 *
 * @param context of the activity
 * @param url to load data from
 */
(context: Context?,
 /** Query URL  */
 private val mUrl: String?) : AsyncTaskLoader<List<News>>(context!!) {

    override fun onStartLoading() {
        // Trigger the loadInBackground() method to execute.
        forceLoad()
    }

    /**
     * This is on a background thread.
     */
    override fun loadInBackground(): List<News>? {
        return if (mUrl == null) {
            null
        } else QueryUtils.fetchNewsData(mUrl)

        // Perform the network request, parse the response, and extract a list of news.
    }

    companion object {

        /** Tag for log messages  */
        private val LOG_TAG = NewsLoader::class.java!!.getName()
    }
}
