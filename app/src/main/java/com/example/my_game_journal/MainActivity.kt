package com.example.my_game_journal

import android.media.session.MediaSession
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_game_journal.databinding.ActivityMainBinding
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private fun fragmentSwitcher() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(BrowsePage({frag -> replaceFragment(frag, true)})) // Default Fragment (page on startup)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.browse -> replaceFragment(BrowsePage { frag -> replaceFragment(frag, true) })
                R.id.search -> replaceFragment(SearchPage())
                R.id.journal -> replaceFragment(JournalPage())
                else -> {
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.getBackStackEntryCount() > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }
}
