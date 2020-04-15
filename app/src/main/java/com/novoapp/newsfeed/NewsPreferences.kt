package com.novoapp.newsfeed

import android.content.Context
import android.net.Uri
import android.preference.PreferenceManager
import com.novoapp.android.newsfeed.R

import com.novoapp.newsfeed.utils.Constants

import com.novoapp.newsfeed.utils.Constants.API_KEY
import com.novoapp.newsfeed.utils.Constants.API_KEY_PARAM
import com.novoapp.newsfeed.utils.Constants.FORMAT
import com.novoapp.newsfeed.utils.Constants.FORMAT_PARAM
import com.novoapp.newsfeed.utils.Constants.FROM_DATE_PARAM
import com.novoapp.newsfeed.utils.Constants.ORDER_BY_PARAM
import com.novoapp.newsfeed.utils.Constants.ORDER_DATE_PARAM
import com.novoapp.newsfeed.utils.Constants.PAGE_SIZE_PARAM
import com.novoapp.newsfeed.utils.Constants.QUERY_PARAM
import com.novoapp.newsfeed.utils.Constants.SECTION_PARAM
import com.novoapp.newsfeed.utils.Constants.SHOW_FIELDS
import com.novoapp.newsfeed.utils.Constants.SHOW_FIELDS_PARAM
import com.novoapp.newsfeed.utils.Constants.SHOW_TAGS
import com.novoapp.newsfeed.utils.Constants.SHOW_TAGS_PARAM

object NewsPreferences {

    /**
     * Get Uri.Builder based on stored SharedPreferences.
     * @param context Context used to access SharedPreferences
     * @return Uri.Builder
     */
    fun getPreferredUri(context: Context): Uri.Builder {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

        // getString retrieves a String value from the preferences. The second parameter is the
        // default value for this preference.
        val numOfItems = sharedPrefs.getString(
                context.getString(R.string.settings_number_of_items_key),
                context.getString(R.string.settings_number_of_items_default))

        // Get the information from SharedPreferences and check for the value associated with the key
        val orderBy = sharedPrefs.getString(
                context.getString(R.string.settings_order_by_key),
                context.getString(R.string.settings_order_by_default))

        // Get the orderDate information from SharedPreferences and check for the value associated with the key
        val orderDate = sharedPrefs.getString(
                context.getString(R.string.settings_order_date_key),
                context.getString(R.string.settings_order_date_default))

        // Get the fromDate information from SharedPreferences and check for the value associated with the key
        val fromDate = sharedPrefs.getString(
                context.getString(R.string.settings_from_date_key),
                context.getString(R.string.settings_from_date_default))

        // Parse breaks apart the URI string that is passed into its parameter
        val baseUri = Uri.parse(Constants.NEWS_REQUEST_URL)

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        val uriBuilder = baseUri.buildUpon()

        // Append query parameter and its value. (e.g. the 'show-tag=contributor')
        uriBuilder.appendQueryParameter(QUERY_PARAM, "")
        uriBuilder.appendQueryParameter(ORDER_BY_PARAM, orderBy)
        uriBuilder.appendQueryParameter(PAGE_SIZE_PARAM, numOfItems)
        uriBuilder.appendQueryParameter(ORDER_DATE_PARAM, orderDate)
        uriBuilder.appendQueryParameter(FROM_DATE_PARAM, fromDate)
        uriBuilder.appendQueryParameter(SHOW_FIELDS_PARAM, SHOW_FIELDS)
        uriBuilder.appendQueryParameter(FORMAT_PARAM, FORMAT)
        uriBuilder.appendQueryParameter(SHOW_TAGS_PARAM, SHOW_TAGS)
        uriBuilder.appendQueryParameter(API_KEY_PARAM, API_KEY) // Use your API key when API rate limit exceeded

        return uriBuilder
    }

    /**
     * Returns String Url for query
     * @param context Context used to access getPreferredUri method
     * @param section News section
     */
    fun getPreferredUrl(context: Context?, section: String): String {
        val uriBuilder = getPreferredUri(context!!)
        return uriBuilder.appendQueryParameter(SECTION_PARAM, section).toString()
    }
}
