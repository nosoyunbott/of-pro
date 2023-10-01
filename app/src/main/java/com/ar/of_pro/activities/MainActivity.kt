package com.ar.of_pro.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ar.of_pro.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ar.of_pro.fragments.RequestsListFragment

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationClient : BottomNavigationView
    lateinit var bottomNavigationProvider : BottomNavigationView
    lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostView) as NavHostFragment

        bottomNavigationClient = findViewById(R.id.bottomNavViewClient) as BottomNavigationView
        bottomNavigationClient = findViewById(R.id.bottomNavViewClient)

        bottomNavigationProvider = findViewById(R.id.bottomNavViewProvider) as BottomNavigationView
        bottomNavigationProvider = findViewById(R.id.bottomNavViewProvider)



        //NavigationUI.setupWithNavController(bottomNavigationClient, navHostFragment.navController)
        NavigationUI.setupWithNavController(bottomNavigationProvider, navHostFragment.navController)
    }



}