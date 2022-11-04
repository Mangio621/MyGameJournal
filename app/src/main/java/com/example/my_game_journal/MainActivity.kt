package com.example.my_game_journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.my_game_journal.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.*


class MainActivity : AppCompatActivity() {
    // Binding for access to the bottomNavigationView
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Create a bottomNavigationView
        val navigator: FragmentNavigator = FragmentNavigator(supportFragmentManager, R.id.frame_layout, binding.bottomNavigationView)
        navigator.replaceFragment(BrowsePage(navigator)) // Default Fragment (page on startup)
        // When a navigation tab is pressed in the bottom menu, check which id is being pressed navigate accordingly
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.browse -> navigator.replaceFragment(BrowsePage(navigator))
                R.id.search -> navigator.replaceFragment(SearchPage(navigator))
                R.id.journal -> navigator.replaceFragment(JournalPage(navigator))
                else -> {
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        // When the back button is pressed, navigate back a fragment via the backstack
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
