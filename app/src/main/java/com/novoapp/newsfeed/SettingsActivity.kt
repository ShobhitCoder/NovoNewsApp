package com.novoapp.newsfeed

import android.app.DatePickerDialog
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.novoapp.android.newsfeed.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * The SettingsActivity is the activity that appears when a settings icon is clicked on.
 */

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        // Navigate with the app icon in the action bar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    /**
     * The NewsPreferenceFragment implements the Preference.OnPreferenceChangeListener interface
     * to set up to listen for any Preference changes made by the user.
     * And the NewsPreferenceFragment also implements the DatePickerDialog.OnDateSetListener to
     * receive a callback when the user has finished selecting a date.
     */
    class NewsPreferenceFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener, DatePickerDialog.OnDateSetListener {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.settings_main)

            // Find the preference for number of items
            val numOfItems = findPreference(getString(R.string.settings_number_of_items_key))
            // bind the current preference value to be displayed
            bindPreferenceSummaryToValue(numOfItems)

            // Find the "order by" Preference object according to its key
            val orderBy = findPreference(getString(R.string.settings_order_by_key))
            // Update the summary so that it displays the current value stored in SharedPreferences
            bindPreferenceSummaryToValue(orderBy)

            // Find the "order date" Preference object according to its key
            val orderDate = findPreference(getString(R.string.settings_order_date_key))
            // Update the summary so that it displays the current value stored in SharedPreferences
            bindPreferenceSummaryToValue(orderDate)

            // Find the "color theme" Preference object according to its key
            val colorTheme = findPreference(getString(R.string.settings_color_key))
            // Update the summary so that it displays the current value stored in SharedPreferences
            bindPreferenceSummaryToValue(colorTheme)

            // Find the "text size" Preference object according to its key
            val textSize = findPreference(getString(R.string.settings_text_size_key))
            // Update the summary so that it displays the current value stored in SharedPreferences
            bindPreferenceSummaryToValue(textSize)

            // Find the "from date" Preference object according to its key
            val fromDate = findPreference(getString(R.string.settings_from_date_key))
            setOnPreferenceClick(fromDate)
            // bind the current preference value to be displayed
            bindPreferenceSummaryToValue(fromDate)
        }

        /**
         * This method is called when the user has clicked a Preference.
         */
        private fun setOnPreferenceClick(preference: Preference) {
            preference.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference ->
                val key = preference.key
                if (key.equals(getString(R.string.settings_from_date_key), ignoreCase = true)) {
                    showDatePicker()
                }
                false
            }
        }

        /**
         * Show the current date as the default date in the picker
         */
        private fun showDatePicker() {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(activity,
                    this, year, month, dayOfMonth).show()
        }

        override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
            var month = month
            // Since it starts counting the months from 0, add one to the month value.
            month = month + 1
            // Date the user selected
            val selectedDate = "$year-$month-$dayOfMonth"
            // Convert selected date string(i.e. "2017-2-1" into formatted date string(i.e. "2017-02-01")
            val formattedDate = formatDate(selectedDate)

            // Storing selected date
            val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
            val editor = prefs.edit()
            editor.putString(getString(R.string.settings_from_date_key), formattedDate).apply()

            // Update the displayed preference summary after it has been changed
            val fromDatePreference = findPreference(getString(R.string.settings_from_date_key))
            bindPreferenceSummaryToValue(fromDatePreference)
        }

        /**
         * This method is called when the user has changed a Preference.
         * Update the displayed preference summary (the UI) after it has been changed.
         * @param preference the changed Preference
         * @param value the new value of the Preference
         * @return True to update the state of the Preference with the new value
         */
        override fun onPreferenceChange(preference: Preference, value: Any): Boolean {
            val stringValue = value.toString()
            // Update the summary of a ListPreference using the label
            if (preference is ListPreference) {
                val prefIndex = preference.findIndexOfValue(stringValue)
                if (prefIndex >= 0) {
                    val labels = preference.entries
                    preference.setSummary(labels[prefIndex])
                }
            } else {
                preference.summary = stringValue
            }
            return true
        }

        /**
         * Set this fragment as the OnPreferenceChangeListener and
         * bind the value that is in SharedPreferences to what will show up in the preference summary
         */
        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the current NewsPreferenceFragment instance to listen for changes to the preference
            // we pass in using
            preference.onPreferenceChangeListener = this

            // Read the current value of the preference stored in the SharedPreferences on the device,
            // and display that in the preference summary
            val preferences = PreferenceManager.getDefaultSharedPreferences(preference.context)
            val preferenceString = preferences.getString(preference.key, "")
            onPreferenceChange(preference, preferenceString!!)
        }

        /**
         * Convert selected date string(i.e. "2017-2-1" into formatted date string(i.e. "2017-02-01")
         *
         * @param dateString is the selected date from the DatePicker
         * @return the formatted date string
         */
        private fun formatDate(dateString: String): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-M-d")
            var dateObject: Date? = null
            try {
                dateObject = simpleDateFormat.parse(dateString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val df = SimpleDateFormat("yyyy-MM-dd")
            return df.format(dateObject!!)
        }
    }

    // Go back to the MainActivity when up button in action bar is clicked on.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
