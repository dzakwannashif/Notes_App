package com.dzakwan.notesapp.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar
import com.dzakwan.notesapp.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this, SplashScreenActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}