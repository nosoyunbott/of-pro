package com.ar.of_pro.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.ar.of_pro.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigation: BottomNavigationView
    lateinit var bottomNavigationProvider: BottomNavigationView
    lateinit var navHostFragment: NavHostFragment
    lateinit var user: String
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        val userType = sharedPreferences.getString("userType", "");

//        val userType: String? = intent?.let {
//            MainActivityArgs.fromBundle(it.extras!!).userType
//        }
        user = userType!!

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostView) as NavHostFragment

        bottomNavigation = findViewById(R.id.bottomNavView)

        if (userType == "PROVIDER") {
            bottomNavigation.menu.findItem(R.id.requestFragment).isVisible = false
        }


        NavigationUI.setupWithNavController(bottomNavigation, navHostFragment.navController)
        bottomNavigation.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.requestsListFragment ->{
                    navHostFragment.navController.navigate(R.id.requestsListFragment)
                    true
                }
                R.id.requestsHistoryFragment -> {
                    navHostFragment.navController.navigate(R.id.requestsHistoryFragment)
                    true
                }
                R.id.profileFragment -> {
                    navHostFragment.navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.requestFragment -> {
                    navHostFragment.navController.navigate(R.id.requestFragment)
                    true
                }
                else -> false
            }
        }
    }



}