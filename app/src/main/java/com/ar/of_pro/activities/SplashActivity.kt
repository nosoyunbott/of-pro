package com.ar.of_pro.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.ar.of_pro.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT : Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(

            {
                startActivity(Intent(this,AuthActivity::class.java))
            }
            , SPLASH_TIME_OUT)
    }
}