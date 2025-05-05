package com.srice
import android.content.Intent
import androidx.viewpager2.widget.MarginPageTransformer
import android.content.res.Configuration
import androidx.viewpager2.widget.CompositePageTransformer
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.srice.R
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private var isDarkMode: Boolean = false
    private lateinit var viewPagers: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var slideHandler: Handler
    private var isScrolling = false
    override fun onDestroy() {
        super.onDestroy()

        // Get the ViewPager2
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        // Get the RecyclerView that ViewPager2 uses internally
        val recyclerView = viewPager.getChildAt(0) as? RecyclerView

        recyclerView?.let { rv ->
            // Iterate through the child views in the RecyclerView
            for (i in 0 until rv.childCount) {
                val viewHolder = rv.findViewHolderForAdapterPosition(i)
                if (viewHolder is SlideAdapter.VideoViewHolder) {
                    // Call releasePlayer() to release the ExoPlayer
                    viewHolder.releasePlayer()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        val sharedPref = getSharedPreferences("ThemePrefs", MODE_PRIVATE)
        isDarkMode = sharedPref.getBoolean("isDarkMode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN
        // Make sure toolbar is initialized BEFORE using it
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Remove default title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Get the ImageView and TextView from the toolbar
        val logoImageView: ImageView = findViewById(R.id.logoImage)
        val titleTextView: TextView = findViewById(R.id.toolbar_title)

        // Set your logo image (you can dynamically change it if needed)
        logoImageView.setImageResource(R.drawable.rice_logo)  // replace with your logo resource

        // Create a SpannableString to style parts of the title
        val titleText = "S Rice"
        val spannableTitle = SpannableString(titleText)

        // Apply green color to the "S"
        spannableTitle.setSpan(ForegroundColorSpan(Color.parseColor("#4A9D5C")), 0, 1, 0)

        // Apply black color to "Rice"
        spannableTitle.setSpan(ForegroundColorSpan(Color.BLACK), 2, titleText.length, 0)

        // Apply bold style to the entire title
        val typeface = ResourcesCompat.getFont(this, R.font.montserrat_bold)
        spannableTitle.setSpan(StyleSpan(android.graphics.Typeface.BOLD), 0, titleText.length, 0)

        // Set the styled title text to the TextView
        titleTextView.text = spannableTitle

        viewPagers = findViewById(R.id.viewPagers)
        tabLayout = findViewById(R.id.tabIndicator)
        slideHandler = Handler(Looper.getMainLooper())

        
        setupSlideCarousel()
        applySlideIndicator()
        applyNavDrawerBackground()


        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = SlideAdapter(this)
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(32))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.85f + r * 0.15f
            page.scaleX = 0.85f + r * 0.15f
            page.alpha = 0.7f + r * 0.3f
        }


        viewPager.setPageTransformer(compositePageTransformer)
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.navigationView)
        val headerView = navigationView.getHeaderView(0)
        val closeIcon = headerView.findViewById<ImageView>(R.id.navCloseIcon)
        closeIcon.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
    private fun setupSlideCarousel() {
        val items = listOf(
            SlideItem(
                "Welcome, Users",
                "We're here to revolutionize your rice farming journey. With SRice, you'll gain powerful tools to boost your yield, improve decision-making, and secure your future."
            ),
            SlideItem(
                "Real-Time Market Pricing",
                "Get the latest market prices directly from producers, retailers, and wholesalers to maximize your profits."
            ),
            SlideItem(
                "Localized Weather Impact Analysis",
                "Stay ahead of typhoons, droughts, and heavy rains with weather insights tailored specifically to your area."
            ),
            SlideItem(
                "Image Processing for Crop Analysis",
                "Scan your rice crops and receive immediate analysis to spot health issues and improve your harvest quality."
            ),
            SlideItem(
                "Weather Forecasting",
                "Predict upcoming weather changes and prepare your fields like a pro farmer."
            ),
            SlideItem(
                "Product Tracking",
                "Follow your rice products from the farm to the market with full transparency and efficiency."
            )
        )

        val adapter = SlideAdapterWelcome(items)
        viewPagers.adapter = adapter

        TabLayoutMediator(tabLayout, viewPagers) { tab, position ->
            // No label needed; dots only
        }.attach()

        // Auto-scroll logic
        slideHandler.postDelayed(object : Runnable {
            override fun run() {
                val nextItem = (viewPagers.currentItem + 1) % items.size
                viewPagers.setCurrentItem(nextItem, true)
                slideHandler.postDelayed(this, 4000) // Change slide every 4 seconds
            }
        }, 4000)
    }

    private fun applyNavDrawerBackground() {
        val navView = findViewById<View>(R.id.navigationView)

        val isDark = (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        val background = LayerDrawable(
            arrayOf(
                ColorDrawable(Color.parseColor(if (isDark) "#000000" else "#FFFFFF")),
                if (isDark) StickerScatterDrawableWhite(this) else StickerScatterDrawable(this)
            )
        )

        navView.background = background
    }

    private fun applySlideIndicator() {
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val indicator1 = findViewById<View>(R.id.indicator2)
        val indicator2 = findViewById<View>(R.id.indicator1)

        viewPager.adapter = SlideAdapter(this)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator1.setBackgroundResource(
                    if (position == 0) R.drawable.indicator_active else R.drawable.indicator_inactive
                )
                indicator2.setBackgroundResource(
                    if (position == 1) R.drawable.indicator_active else R.drawable.indicator_inactive
                )
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        // Customize the "Join Us Today" menu item
        val loginMenuItem = menu?.findItem(R.id.action_login)
        loginMenuItem?.setActionView(R.layout.custom_menu_button)

        // Set click listener on the custom view
        val customButton = loginMenuItem?.actionView
        customButton?.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            val intent = Intent(this, LoginSignup::class.java)
            startActivity(intent)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_toggle_theme -> {
                // Toggle theme
                isDarkMode = !isDarkMode
                getSharedPreferences("ThemePrefs", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isDarkMode", isDarkMode)
                    .apply()

                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
                recreate()
                true
            }


            R.id.action_login -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }

                val intent = Intent(this, LoginSignup::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }




}
