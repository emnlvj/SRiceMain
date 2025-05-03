package com.example

import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sricedemo.R

class HomePage : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var menuButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        // Hide the action bar
        supportActionBar?.hide()

        // Initialize DrawerLayout and Menu Button
        drawerLayout = findViewById(R.id.drawer_layout)
        menuButton = findViewById(R.id.menu_button)

        // Set OnClickListener to the menu button to open the drawer
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT) // or Gravity.START, depending on your layout
        }
    }
}
