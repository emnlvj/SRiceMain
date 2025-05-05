package com.srice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ViewAnimator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.srice.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProfilePage : AppCompatActivity() {

    private var isBottomNavVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val scrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        val fab = findViewById<FloatingActionButton>(R.id.fabCenter)
        bottomNav.selectedItemId = R.id.nav_profile

        // Scroll listener to hide/show bottom nav
        var lastScrollY = 0
        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY && isBottomNavVisible) {
                // scrolling down -> hide
                bottomNav.animate().translationY(bottomNav.height.toFloat()).setDuration(200).start()
                fab.animate().translationY(fab.height.toFloat() + 300).setDuration(200).start()
                isBottomNavVisible = false
            } else if (scrollY < oldScrollY && !isBottomNavVisible) {
                // scrolling up -> show
                bottomNav.animate().translationY(0f).setDuration(200).start()
                fab.animate().translationY(0f).setDuration(200).start()
                isBottomNavVisible = true
            }
        })

        // Bottom Nav Clicks
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_alerts -> {
                    // TODO: Navigate to Alerts activity
                    true
                }
                R.id.nav_scan-> {
                    // TODO: Navigate to Settings activity
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }

        // ViewAnimator content switcher
        val viewAnimator = findViewById<ViewAnimator>(R.id.cardContentSwitcher)

        val aboutArrow = findViewById<ImageView>(R.id.about_right_arrow)
        val backArrow = findViewById<ImageView>(R.id.aboutBackArrow)

        aboutArrow.setOnClickListener {
            viewAnimator.outAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            viewAnimator.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
            viewAnimator.displayedChild = 1
        }

        backArrow.setOnClickListener {
            viewAnimator.outAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            viewAnimator.inAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            viewAnimator.displayedChild = 0
        }
    }
}
