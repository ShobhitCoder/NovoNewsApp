package com.novoapp.newsfeed.utils

/**
 * Store Constants for the NewsFeed app.
 */

object Constants {

    /**  Extract the key associated with the JSONObject  */
    internal val JSON_KEY_RESPONSE = "response"
    internal val JSON_KEY_RESULTS = "results"
    internal val JSON_KEY_WEB_TITLE = "webTitle"
    internal val JSON_KEY_SECTION_NAME = "sectionName"
    internal val JSON_KEY_WEB_PUBLICATION_DATE = "webPublicationDate"
    internal val JSON_KEY_WEB_URL = "webUrl"
    internal val JSON_KEY_TAGS = "tags"
    internal val JSON_KEY_FIELDS = "fields"
    internal val JSON_KEY_THUMBNAIL = "thumbnail"
    internal val JSON_KEY_TRAIL_TEXT = "trailText"

    /** Read timeout for setting up the HTTP request  */
    internal val READ_TIMEOUT = 10000 /* milliseconds */

    /** Connect timeout for setting up the HTTP request  */
    internal val CONNECT_TIMEOUT = 15000 /* milliseconds */

    /** HTTP response code when the request is successful  */
    internal val SUCCESS_RESPONSE_CODE = 200

    /** Request method type "GET" for reading information from the server  */
    internal val REQUEST_METHOD_GET = "GET"

    /** URL for news data from the guardian data set  */
    val NEWS_REQUEST_URL = "https://content.guardianapis.com/search"

    /** Parameters  */
    val QUERY_PARAM = "q"
    val ORDER_BY_PARAM = "order-by"
    val PAGE_SIZE_PARAM = "page-size"
    val ORDER_DATE_PARAM = "order-date"
    val FROM_DATE_PARAM = "from-date"
    val SHOW_FIELDS_PARAM = "show-fields"
    val FORMAT_PARAM = "format"
    val SHOW_TAGS_PARAM = "show-tags"
    val API_KEY_PARAM = "api-key"
    val SECTION_PARAM = "section"

    /** The show fields we want our API to return  */
    val SHOW_FIELDS = "thumbnail,trailText"

    /** The format we want our API to return  */
    val FORMAT = "json"

    /** The show tags we want our API to return  */
    val SHOW_TAGS = "contributor"

    /** API Key  */
    val API_KEY = "test" // Use your API Key when API rate limit exceeded

    /** Default number to set the image on the top of the textView  */
    val DEFAULT_NUMBER = 0

    /** Constants value for each fragment  */
    val HOME = 0
    val WORLD = 1
    val SCIENCE = 2
    val SPORT = 3
    val ENVIRONMENT = 4
    val SOCIETY = 5
    val FASHION = 6
    val BUSINESS = 7
    val CULTURE = 8

}
/**
 * Create a private constructor because no one should ever create a [Constants] object.
 */
