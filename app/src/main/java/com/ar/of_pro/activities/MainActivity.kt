package com.ar.of_pro.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ar.of_pro.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ar.of_pro.fragments.HistoryFragment
import com.ar.of_pro.fragments.RequestFragment
import com.ar.of_pro.fragments.RequestsListFragment

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigation : BottomNavigationView
    lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavView) as BottomNavigationView
        navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostView) as NavHostFragment

        bottomNavigation = findViewById(R.id.bottomNavView)

        NavigationUI.setupWithNavController(bottomNavigation, navHostFragment.navController)
        /*bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.requestsListFragment -> {
                    loadFragment(RequestsListFragment())
                    true
                }
                R.id.historyFragment -> {
                    loadFragment(HistoryFragment())
                    true
                }
                R.id.requestFragment -> {
                    loadFragment(RequestFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }*/
    }
   /* private fun loadFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragments = fragmentManager.fragments

        if (!fragments.isEmpty()) {
            val transaction = fragmentManager.beginTransaction()
            for (currentFragment in fragments) {
                transaction.remove(currentFragment)
            }
            transaction.commitNow()
        }

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.mainLayout, fragment)
        transaction.commit()
    }*/
}