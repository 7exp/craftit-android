package com.sevenexp.craftit.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var alreadySearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        if (navHostFragment != null) {
            val navController = navHostFragment.findNavController()
            navView.setupWithNavController(navController)
        } else {
            throw RuntimeException("Invalid NavHostFragment id")
        }
    }

    fun checkSearchState(status: Boolean): Boolean {
        return alreadySearch
    }
}