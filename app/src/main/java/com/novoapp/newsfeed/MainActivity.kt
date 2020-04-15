package com.novoapp.newsfeed

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.novoapp.android.newsfeed.R
import com.novoapp.newsfeed.adapter.CategoryFragmentPagerAdapter
import com.novoapp.newsfeed.utils.Constants

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = findViewById(R.id.viewpager)

        // Give the TabLayout the ViewPager
        val tabLayout = findViewById<TabLayout>(R.id.sliding_tabs)
        tabLayout.setupWithViewPager(viewPager)
        // Set gravity for tab bar
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        assert(navigationView != null)
        navigationView!!.setNavigationItemSelectedListener(this)

        // Set the default fragment when starting the app
        onNavigationItemSelected(navigationView.menu.getItem(0).setChecked(true))

        // Set category fragment pager adapter
        val pagerAdapter = CategoryFragmentPagerAdapter(this, supportFragmentManager)
        // Set the pager adapter onto the view pager
        viewPager!!.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        // Switch Fragments in a ViewPager on clicking items in Navigation Drawer
        if (id == R.id.nav_home) {
            viewPager!!.currentItem = Constants.HOME
        } else if (id == R.id.nav_world) {
            viewPager!!.currentItem = Constants.WORLD
        } else if (id == R.id.nav_science) {
            viewPager!!.currentItem = Constants.SCIENCE
        } else if (id == R.id.nav_sport) {
            viewPager!!.currentItem = Constants.SPORT
        } else if (id == R.id.nav_environment) {
            viewPager!!.currentItem = Constants.ENVIRONMENT
        } else if (id == R.id.nav_society) {
            viewPager!!.currentItem = Constants.SOCIETY
        } else if (id == R.id.nav_fashion) {
            viewPager!!.currentItem = Constants.FASHION
        } else if (id == R.id.nav_business) {
            viewPager!!.currentItem = Constants.BUSINESS
        } else if (id == R.id.nav_culture) {
            viewPager!!.currentItem = Constants.CULTURE
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override// Initialize the contents of the Activity's options menu
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the Options Menu we specified in XML
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override// This method is called whenever an item in the options menu is selected.
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
