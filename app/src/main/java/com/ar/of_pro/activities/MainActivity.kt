package com.ar.of_pro.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ar.of_pro.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.ar.of_pro.fragments.HistoryFragment
import com.ar.of_pro.fragments.RequestFragment
import com.ar.of_pro.fragments.RequestsListFragment

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNavView) as BottomNavigationView

        bottomNav.setOnItemSelectedListener {
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
        }
    }
    private fun loadFragment(fragment: Fragment) {
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
    }
}