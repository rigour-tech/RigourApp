package com.dash.rigour.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dash.rigour.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        var navController = findNavController(R.id.fragment)
        var bottomNavigationItemView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationItemView.setupWithNavController(navController)


    }
}