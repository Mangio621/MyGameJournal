package com.example.my_game_journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.my_game_journal.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.io.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navigator: FragmentNavigator = FragmentNavigator(supportFragmentManager, R.id.frame_layout)
        navigator.replaceFragment(BrowsePage(navigator)) // Default Fragment (page on startup)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.browse -> navigator.replaceFragment(BrowsePage(navigator))
                R.id.search -> navigator.replaceFragment(SearchPage())
                R.id.journal -> navigator.replaceFragment(JournalPage())
                else -> {
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
