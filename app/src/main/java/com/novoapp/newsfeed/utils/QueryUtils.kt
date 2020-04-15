package com.novoapp.newsfeed.utils

import android.text.TextUtils
import android.util.Log
import com.novoapp.newsfeed.News
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.util.*

/**
 * Helper methods related to requesting and receiving news data from Guardian.
 */
object QueryUtils {

    /** Tag for the log messages  */
    private val LOG_TAG = QueryUtils::class.java!!.getSimpleName()

    /**
     * Query the Guardian data set and return a list of [News] objects.
     */
    fun fetchNewsData(requestUrl: String): List<News>? {
        // Create URL object
        val url = createUrl(requestUrl)

        // Perform HTTP request to the URL and receive a JSON response back
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpRequest(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e)
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}

        // Return the list of {@link News}
        return extractFeatureFromJSON(jsonResponse)
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private fun createUrl(stringUrl: String): URL? {
        var url: URL? = null
        try {
            url = URL(stringUrl)
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, "Problem building the URL.", e)
        }

        return url
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    @Throws(IOException::class)
    private fun makeHttpRequest(url: URL?): String {
        var jsonResponse = ""

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse
        }

        var urlConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.readTimeout = Constants.READ_TIMEOUT
            urlConnection.connectTimeout = Constants.CONNECT_TIMEOUT
            urlConnection.requestMethod = Constants.REQUEST_METHOD_GET
            urlConnection.connect()

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.responseCode == Constants.SUCCESS_RESPONSE_CODE) {
                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.responseCode)
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e)
        } finally {
            urlConnection?.disconnect()
            inputStream?.close()
        }
        return jsonResponse
    }

    /**
     * Convert the [InputStream] into a String which contains the
     * whole JSON response from the server.
     */
    @Throws(IOException::class)
    private fun readFromStream(inputStream: InputStream?): String {
        val output = StringBuilder()
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val reader = BufferedReader(inputStreamReader)
            var line: String? = reader.readLine()
            while (line != null) {
                output.append(line)
                line = reader.readLine()
            }
        }
        return output.toString()
    }

    /**
     * Return a list of [News] objects that has been built up from
     * parsing the given JSON response.
     */
    private fun extractFeatureFromJSON(newsJSON: String?): List<News>? {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null
        }
        // Create an empty ArrayList that we can start adding news to
        val newsList = ArrayList<News>()

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        try {
            // Create a JSONObject from the JSON response string
            val baseJsonResponse = JSONObject(newsJSON!!)

            // Extract the JSONObject associated with the key called "response"
            val responseJsonObject = baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE)

            // Extract the JSONArray associated with the key called "results"
            val resultsArray = responseJsonObject.getJSONArray(Constants.JSON_KEY_RESULTS)

            // For each element in the resultsArray, create a {@link News} object
            for (i in 0 until resultsArray.length()) {

                // Get a single news at position i within the list of news
                val currentNews = resultsArray.getJSONObject(i)
                // For a given news, extract the value for the key called "webTitle"
                val webTitle = currentNews.getString(Constants.JSON_KEY_WEB_TITLE)
                // For a given news, extract the value for the key called "sectionName"
                val sectionName = currentNews.getString(Constants.JSON_KEY_SECTION_NAME)
                // For a given news, extract the value for the key called "webPublicationDate"
                val webPublicationDate = currentNews.getString(Constants.JSON_KEY_WEB_PUBLICATION_DATE)
                // For a given news, extract the value for the key called "webUrl"
                val webUrl = currentNews.getString(Constants.JSON_KEY_WEB_URL)

                // For a given news, if it contains the key called "tags", extract JSONArray
                // associated with the key "tags"
                var author: String? = null
                if (currentNews.has(Constants.JSON_KEY_TAGS)) {
                    // Extract the JSONArray associated with the key called "tags"
                    val tagsArray = currentNews.getJSONArray(Constants.JSON_KEY_TAGS)
                    if (tagsArray.length() != 0) {
                        // Extract the first JSONObject in the tagsArray
                        val firstTagsItem = tagsArray.getJSONObject(0)
                        // Extract the value for the key called "webTitle"
                        author = firstTagsItem.getString(Constants.JSON_KEY_WEB_TITLE)
                    }
                }

                // For a given news, if it contains the key called "fields", extract JSONObject
                // associated with the key "fields"
                var thumbnail: String? = null
                var trailText: String? = null
                if (currentNews.has(Constants.JSON_KEY_FIELDS)) {
                    // Extract the JSONObject associated with the key called "fields"
                    val fieldsObject = currentNews.getJSONObject(Constants.JSON_KEY_FIELDS)
                    // If there is the key called "thumbnail", extract the value for the key called "thumbnail"
                    if (fieldsObject.has(Constants.JSON_KEY_THUMBNAIL)) {
                        thumbnail = fieldsObject.getString(Constants.JSON_KEY_THUMBNAIL)
                    }
                    // If there is the key called "trailText", extract the value for the key called "trailText"
                    if (fieldsObject.has(Constants.JSON_KEY_TRAIL_TEXT)) {
                        trailText = fieldsObject.getString(Constants.JSON_KEY_TRAIL_TEXT)
                    }
                }

                // Create a new {@link News} object with the title and url from the JSON response.
                val news = News(webTitle, sectionName, author, webPublicationDate, webUrl, thumbnail, trailText)

                // Add the new {@link News} to list of newsList.
                newsList.add(news)
            }
        } catch (e: JSONException) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e)
        }

        // Return the list of news
        return newsList
    }
}
/**
 * Create a private constructor because no one should ever create a [QueryUtils] object.
 */
