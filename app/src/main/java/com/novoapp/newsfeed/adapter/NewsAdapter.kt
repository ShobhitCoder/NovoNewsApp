package com.novoapp.newsfeed.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.preference.PreferenceManager
import android.text.Html
import android.text.format.DateUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.News
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * A [NewsAdapter] can provide a card item layout for each news in the data source
 * ( a list of [News] objects).
 */

class NewsAdapter
/**
 * Constructs a new [NewsAdapter]
 * @param context of the app
 * @param newsList is the list of news, which is the data source of the adapter
 */
(private val mContext: Context?, private val mNewsList: MutableList<News>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.news_card_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mNewsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView
         val sectionTextView: TextView
         val authorTextView: TextView
         val dateTextView: TextView
         val thumbnailImageView: ImageView
         val shareImageView: ImageView
         val trailTextView: TextView
         val cardView: CardView

        init {
            titleTextView = itemView.findViewById(R.id.title_card)
            sectionTextView = itemView.findViewById(R.id.section_card)
            authorTextView = itemView.findViewById(R.id.author_card)
            dateTextView = itemView.findViewById(R.id.date_card)
            thumbnailImageView = itemView.findViewById(R.id.thumbnail_image_card)
            shareImageView = itemView.findViewById(R.id.share_image_card)
            trailTextView = itemView.findViewById(R.id.trail_text_card)
            cardView = itemView.findViewById(R.id.card_view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext)

        // Change the color theme of Title TextView by using the user's stored preferences
        setColorTheme(holder)

        // Change text size of TextView by using the user's stored preferences
        setTextSize(holder)

        // Find the current news that was clicked on
        val currentNews = mNewsList[position]

        holder.titleTextView.text = currentNews.title
        holder.sectionTextView.text = currentNews.section
        // If the author does not exist, hide the authorTextView
        if (currentNews.author == null) {
            holder.authorTextView.visibility = View.GONE
        } else {
            holder.authorTextView.visibility = View.VISIBLE
            holder.authorTextView.text = currentNews.author
        }

        // Get time difference between the current date and web publication date and
        // set the time difference on the textView
        holder.dateTextView.text = getTimeDifference(formatDate(currentNews.date))

        // Get string of the trailTextHTML and convert Html text to plain text
        // and set the plain text on the textView
        val trailTextHTML = currentNews.trailTextHtml
        holder.trailTextView.text = Html.fromHtml(Html.fromHtml(trailTextHTML).toString())

        // Set an OnClickListener to open a website with more information about the selected article
        holder.cardView.setOnClickListener {
            // Convert the String URL into a URI object (to pass into the Intent constructor)
            val newsUri = Uri.parse(currentNews.url)

            // Create a new intent to view the news URI
            val websiteIntent = Intent(Intent.ACTION_VIEW, newsUri)

            // Send the intent to launch a new activity
            mContext?.startActivity(websiteIntent)
        }

        if (currentNews.thumbnail == null) {
            holder.thumbnailImageView.visibility = View.GONE
        } else {
            holder.thumbnailImageView.visibility = View.VISIBLE
            // Load thumbnail with glide
            mContext?.applicationContext?.let {
                Glide.with(it)
                        .load(currentNews.thumbnail)
                        .into(holder.thumbnailImageView)
            }
        }
        // Set an OnClickListener to share the data with friends via email or  social networking
        holder.shareImageView.setOnClickListener { shareData(currentNews) }
    }

    /**
     * Set the user preferred color theme
     */
    private fun setColorTheme(holder: ViewHolder) {
        // Get the color theme string from SharedPreferences and check for the value associated with the key
        val colorTheme = sharedPrefs!!.getString(
                mContext?.getString(R.string.settings_color_key),
                mContext?.getString(R.string.settings_color_default))

        // Change the background color of titleTextView by using the user's stored preferences
        if (colorTheme == mContext?.getString(R.string.settings_color_white_value)) {
            holder.titleTextView.setBackgroundResource(R.color.white)
            holder.titleTextView.setTextColor(Color.BLACK)
        } else if (colorTheme == mContext?.getString(R.string.settings_color_sky_blue_value)) {
            holder.titleTextView.setBackgroundResource(R.color.nav_bar_start)
            holder.titleTextView.setTextColor(Color.WHITE)
        } else if (colorTheme == mContext?.getString(R.string.settings_color_dark_blue_value)) {
            holder.titleTextView.setBackgroundResource(R.color.color_app_bar_text)
            holder.titleTextView.setTextColor(Color.WHITE)
        } else if (colorTheme == mContext?.getString(R.string.settings_color_violet_value)) {
            holder.titleTextView.setBackgroundResource(R.color.violet)
            holder.titleTextView.setTextColor(Color.WHITE)
        } else if (colorTheme == mContext?.getString(R.string.settings_color_light_green_value)) {
            holder.titleTextView.setBackgroundResource(R.color.light_green)
            holder.titleTextView.setTextColor(Color.WHITE)
        } else if (colorTheme == mContext?.getString(R.string.settings_color_green_value)) {
            holder.titleTextView.setBackgroundResource(R.color.color_section)
            holder.titleTextView.setTextColor(Color.WHITE)
        }
    }

    /**
     * Set the text size to the text size the user choose.
     */
    private fun setTextSize(holder: ViewHolder) {
        // Get the text size string from SharedPreferences and check for the value associated with the key
        val textSize = sharedPrefs!!.getString(
                mContext?.getString(R.string.settings_text_size_key),
                mContext?.getString(R.string.settings_text_size_default))

        // Change text size of TextView by using the user's stored preferences
        if (textSize == mContext?.getString(R.string.settings_text_size_medium_value)) {
            mContext?.resources?.getDimension(R.dimen.sp22)?.let {
                holder.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp14)?.let {
                holder.sectionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp16)?.let {
                holder.trailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp14)?.let {
                holder.authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp14)?.let {
                holder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
        } else if (textSize == mContext?.getString(R.string.settings_text_size_small_value)) {
            mContext?.resources?.getDimension(R.dimen.sp20)?.let {
                holder.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp12)?.let {
                holder.sectionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp14)?.let {
                holder.trailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp12)?.let {
                holder.authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp12)?.let {
                holder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
        } else if (textSize == mContext?.getString(R.string.settings_text_size_large_value)) {
            mContext?.resources?.getDimension(R.dimen.sp24)?.let {
                holder.titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp16)?.let {
                holder.sectionTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp18)?.let {
                holder.trailTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp16)?.let {
                holder.authorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
            mContext?.resources?.getDimension(R.dimen.sp16)?.let {
                holder.dateTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        it)
            }
        }
    }

    /**
     * Share the article with friends in social network
     * @param news [News] object
     */
    private fun shareData(news: News) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                news.title + " : " + news.url)
        mContext?.startActivity(Intent.createChooser(sharingIntent,
                mContext.getString(R.string.share_article)))
    }

    /**
     * Clear all data (a list of [News] objects)
     */
    fun clearAll() {
        mNewsList.clear()
        notifyDataSetChanged()
    }

    /**
     * Add  a list of [News]
     * @param newsList is the list of news, which is the data source of the adapter
     */
    fun addAll(newsList: List<News>) {
        mNewsList.clear()
        mNewsList.addAll(newsList)
        notifyDataSetChanged()
    }

    /**
     * Convert date and time in UTC (webPublicationDate) into a more readable representation
     * in Local time
     *
     * @param dateStringUTC is the web publication date of the article (i.e. 2014-02-04T08:00:00Z)
     * @return the formatted date string in Local time(i.e "Jan 1, 2000  2:15 AM")
     * from a date and time in UTC
     */
    private fun formatDate(dateStringUTC: String): String {
        // Parse the dateString into a Date object
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'")
        var dateObject: Date? = null
        try {
            dateObject = simpleDateFormat.parse(dateStringUTC)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        // Initialize a SimpleDateFormat instance and configure it to provide a more readable
        // representation according to the given format, but still in UTC
        val df = SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.ENGLISH)
        val formattedDateUTC = df.format(dateObject!!)
        // Convert UTC into Local time
        df.timeZone = TimeZone.getTimeZone("UTC")
        var date: Date? = null
        try {
            date = df.parse(formattedDateUTC)
            df.timeZone = TimeZone.getDefault()
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return df.format(date!!)
    }

    /**
     * Get the formatted web publication date string in milliseconds
     * @param formattedDate the formatted web publication date string
     * @return the formatted web publication date in milliseconds
     */
    private fun getDateInMillis(formattedDate: String): Long {
        val simpleDateFormat = SimpleDateFormat("MMM d, yyyy  h:mm a")
        val dateInMillis: Long
        val dateObject: Date?
        try {
            dateObject = simpleDateFormat.parse(formattedDate)
            dateInMillis = dateObject!!.time
            return dateInMillis
        } catch (e: ParseException) {
            Log.e("Problem parsing date", e.message)
            e.printStackTrace()
        }

        return 0
    }

    /**
     * Get the time difference between the current date and web publication date
     * @param formattedDate the formatted web publication date string
     * @return time difference (i.e "9 hours ago")
     */
    private fun getTimeDifference(formattedDate: String): CharSequence {
        val currentTime = System.currentTimeMillis()
        val publicationTime = getDateInMillis(formattedDate)
        return DateUtils.getRelativeTimeSpanString(publicationTime, currentTime,
                DateUtils.SECOND_IN_MILLIS)
    }
}
