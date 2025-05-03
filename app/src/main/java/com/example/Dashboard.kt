package com.example

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.sricedemo.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Dashboard : AppCompatActivity() {
    private var isBottomNavVisible = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationdash)
        val scrollView = findViewById<NestedScrollView>(R.id.nestedScrollViewdash)
        val fab = findViewById<FloatingActionButton>(R.id.fabCenterdash)
        bottomNav.selectedItemId = R.id.nav_home

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
        val recyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Optional: smoother scrolling
        recyclerView.setHasFixedSize(true)

        // Attach snap helper for center item snapping
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // Set adapter with your task card layouts
        val taskList = listOf(
            R.layout.task_card1,
            R.layout.task_card2,
            R.layout.task_card3,
            R.layout.task_card4,
            R.layout.task_card5
        )
        recyclerView.adapter = TaskAdapter(this, taskList)

        // Add ItemDecoration for spacing between cards
        val spacingDecoration = object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.right = 16 // Add margin to the right of each item
            }
        }
        recyclerView.addItemDecoration(spacingDecoration)

        // Handle user scrolling behavior for snapping
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // Find the closest view to the center and snap to it
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

                    // Get the central view (between first and last visible items)
                    val centralViewPosition = (firstVisiblePosition + lastVisiblePosition) / 2
                    layoutManager.scrollToPositionWithOffset(centralViewPosition, 0)
                }
            }
        })

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> true

                R.id.nav_alerts -> {
                    // TODO: Navigate to Alerts activity
                    true
                }
                R.id.nav_scan ->
                {
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfilePage::class.java)
                    startActivity(intent)

                    true
                }
                else -> false
            }
        }
    }
}
