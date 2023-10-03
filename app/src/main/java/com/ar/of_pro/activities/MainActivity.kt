package com.ar.of_pro.activities

import android.content.ClipData.Item
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.ar.of_pro.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigation : BottomNavigationView
    lateinit var bottomNavigationProvider : BottomNavigationView
    lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userType: String? = intent?.let {
            MainActivityArgs.fromBundle(it.extras!!).userType
        }

        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostView) as NavHostFragment

        bottomNavigation = findViewById(R.id.bottomNavView)
        if(userType == "CLIENT"){
            bottomNavigation.menu.findItem(R.id.providerProfileFragment).isVisible = false
        }else{
            bottomNavigation.menu.findItem(R.id.requestFragment).isVisible = false
        }


        NavigationUI.setupWithNavController(bottomNavigation, navHostFragment.navController)

    }



}