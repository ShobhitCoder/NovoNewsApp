package com.novoapp.newsfeed

/**
 * An [News] object contains information related to a single news.
 */

class News
/**
 * Constructs a new [News] object.
 *
 * @param title is the title of the article
 * @param section is the section name of the article
 * @param author is author name in article
 * @param date is the web publication date of the article
 * @param url is the website URL to find more details about the article
 * @param thumbnail is the thumbnail of the article
 * @param trailText is trail text of the article with string type Html
 */
(
        /** Title of the article  */
        /**
         * Returns the title of the article
         */
        val title: String,
        /** Section name of the article */
        /**
         * Returns the section name of the article.
         */
        val section: String,
        /** Author name in the article  */
        /**
         * Returns the author name of the article.
         */
        val author: String?,
        /** Web publication date of the article  */
        /**
         * Returns the web publication date of the article.
         */
        val date: String,
        /** Website URL of the article  */
        /**
         * Returns the website URL to find more information about the news.
         */
        val url: String,
        /** Thumbnail of the article  */
        /**
         * Returns the thumbnail of the article
         */
        val thumbnail: String?,
        /** TrailText of the article with string type Html  */
        /**
         * Returns the TrailText of the article with string type Html
         */
        val trailTextHtml: String?)
