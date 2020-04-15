package com.novoapp.newsfeed.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.EmptyRecyclerView
import com.novoapp.newsfeed.News
import com.novoapp.newsfeed.NewsLoader
import com.novoapp.newsfeed.NewsPreferences
import com.novoapp.newsfeed.adapter.NewsAdapter
import com.novoapp.newsfeed.utils.Constants

import java.util.ArrayList

/**
 * The BaseArticlesFragment is a [Fragment] subclass that implements the LoaderManager.LoaderCallbacks
 * interface in order for Fragment to be a client that interacts with the LoaderManager. It is
 * base class that is responsible for displaying a set of articles, regardless of type.
 */
open class BaseArticlesFragment : Fragment(), LoaderManager.LoaderCallbacks<List<News>> {

    /** Adapter for the list of news  */
    private var mAdapter: NewsAdapter? = null

    /** TextView that is displayed when the recycler view is empty  */
    private var mEmptyStateTextView: TextView? = null

    /** Loading indicator that is displayed before the first load is completed  */
    private var mLoadingIndicator: View? = null

    /** The [SwipeRefreshLayout] that detects swipe gestures and
     * triggers callbacks in the app.
     */
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    /**
     * Check for network connectivity.
     */
    private// Get a reference to the ConnectivityManager to check state of network connectivity
    // Get details on the currently active default data network
    val isConnected: Boolean
        get() {
            val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            return networkInfo != null && networkInfo.isConnected
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Find a reference to the {@link RecyclerView} in the layout
        // Replaced RecyclerView with EmptyRecyclerView
        val mRecyclerView = rootView.findViewById<EmptyRecyclerView>(R.id.recycler_view)
        val layoutManager = LinearLayoutManager(activity)
        mRecyclerView.setHasFixedSize(true)

        // Set the layoutManager on the {@link RecyclerView}
        mRecyclerView.layoutManager = layoutManager

        // Find the SwipeRefreshLayout
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh)
        // Set the color scheme of the SwipeRefreshLayout
        mSwipeRefreshLayout!!.setColorSchemeColors(resources.getColor(R.color.swipe_color_1),
                resources.getColor(R.color.swipe_color_2),
                resources.getColor(R.color.swipe_color_3),
                resources.getColor(R.color.swipe_color_4))

        // Set up OnRefreshListener that is invoked when the user performs a swipe-to-refresh gesture.
        mSwipeRefreshLayout!!.setOnRefreshListener {
            Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout")
            // restart the loader
            initiateRefresh()
            Toast.makeText(activity, getString(R.string.updated_just_now),
                    Toast.LENGTH_SHORT).show()
        }

        // Find the loading indicator from the layout
        mLoadingIndicator = rootView.findViewById(R.id.loading_indicator)

        // Find the empty view from the layout and set it on the new recycler view
        mEmptyStateTextView = rootView.findViewById(R.id.empty_view)
        mRecyclerView.setEmptyView(mEmptyStateTextView)

        // Create a new adapter that takes an empty list of news as input
        mAdapter = NewsAdapter(activity, ArrayList())

        // Set the adapter on the {@link recyclerView}
        mRecyclerView.adapter = mAdapter

        // Check for network connectivity and initialize the loader
        initializeLoader(isConnected)

        return rootView
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<News>> {

        val uriBuilder = NewsPreferences.getPreferredUri(context!!)

        Log.e(LOG_TAG, uriBuilder.toString())

        // Create a new loader for the given URL
        return NewsLoader(activity, uriBuilder.toString())
    }

    override fun onLoadFinished(loader: Loader<List<News>>, newsData: List<News>?) {
        // Hide loading indicator because the data has been loaded
        mLoadingIndicator!!.visibility = View.GONE

        // Set empty state text to display "No news found."
        mEmptyStateTextView!!.setText(R.string.no_news)

        // Clear the adapter of previous news data
        mAdapter!!.clearAll()

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the recyclerView to update.
        if (newsData != null && !newsData.isEmpty()) {
            mAdapter!!.addAll(newsData)
        }

        // Hide the swipe icon animation when the loader is done refreshing the data
        mSwipeRefreshLayout!!.isRefreshing = false
    }

    override fun onLoaderReset(loader: Loader<List<News>>) {
        // Loader reset, so we can clear out our existing data.
        mAdapter!!.clearAll()
    }

    /**
     * When the user returns to the previous screen by pressing the up button in the SettingsActivity,
     * restart the Loader to reflect the current value of the preference.
     */
    override fun onResume() {
        super.onResume()
        restartLoader(isConnected)
    }

    /**
     * If there is internet connectivity, initialize the loader as
     * usual. Otherwise, hide loading indicator and set empty state TextView to display
     * "No internet connection."
     *
     * @param isConnected internet connection is available or not
     */
    private fun initializeLoader(isConnected: Boolean) {
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            val loaderManager = loaderManager
            // Initialize the loader with the NEWS_LOADER_ID
            loaderManager.initLoader(NEWS_LOADER_ID, null, this)
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator!!.visibility = View.GONE
            // Update empty state with no connection error message and image
            mEmptyStateTextView!!.setText(R.string.no_internet_connection)
            mEmptyStateTextView!!.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check, Constants.DEFAULT_NUMBER, Constants.DEFAULT_NUMBER)
        }
    }

    /**
     * Restart the loader if there is internet connectivity.
     * @param isConnected internet connection is available or not
     */
    private fun restartLoader(isConnected: Boolean) {
        if (isConnected) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            val loaderManager = loaderManager
            // Restart the loader with the NEWS_LOADER_ID
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this)
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            mLoadingIndicator!!.visibility = View.GONE
            // Update empty state with no connection error message and image
            mEmptyStateTextView!!.setText(R.string.no_internet_connection)
            mEmptyStateTextView!!.setCompoundDrawablesWithIntrinsicBounds(Constants.DEFAULT_NUMBER,
                    R.drawable.ic_network_check, Constants.DEFAULT_NUMBER, Constants.DEFAULT_NUMBER)

            // Hide SwipeRefreshLayout
            mSwipeRefreshLayout!!.visibility = View.GONE
        }
    }

    /**
     * When the user performs a swipe-to-refresh gesture, restart the loader.
     */
    private fun initiateRefresh() {
        restartLoader(isConnected)
    }

    companion object {

        private val LOG_TAG = BaseArticlesFragment::class.java.getName()

        /** Constant value for the news loader ID.  */
        private val NEWS_LOADER_ID = 1
    }
}
