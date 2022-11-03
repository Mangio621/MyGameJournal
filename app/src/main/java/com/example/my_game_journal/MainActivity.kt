package com.example.my_game_journal

import android.media.session.MediaSession
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.my_game_journal.databinding.ActivityMainBinding
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(BrowsePage()) // Default Fragment (page on startup)

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId) {
                R.id.browse -> replaceFragment(BrowsePage())
                R.id.search -> replaceFragment(SearchPage())
                R.id.journal -> replaceFragment(JournalPage())
                else -> {

                }

            }
            true
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }




    fun test() {
        val url = "https://id.twitch.tv/oauth2/token?client_id=lo0phw9dgi0gei5pj5w9tnlooqgp5f&client_secret=w4mk3sm0xn151umyosja37qhj19sku&grant_type=client_credentials"
        val async = url.httpPost().responseString { request, response, result ->
            if(result is Result.Success) {
                val data = result.get()
                val gson = Gson()
                val auth = gson.fromJson(data, AccessTokenInfo::class.java)
                Log.i("API", auth.access_token)
                getTestgames(auth)
            }
        }
    }

    fun getTestgames(auth: AccessTokenInfo) {
        val url = "https://api.igdb.com/v4/games"
        val httpAsync = url.httpPost()
            .header(
                "Client-ID" to "lo0phw9dgi0gei5pj5w9tnlooqgp5f",
                "Authorization" to "Bearer ${auth.access_token}",
                //"Access" to "application/json",
                //"Content-Type" to "text/plain",
                //"Accept-Encoding" to "gzip, deflate, br",
                //"Accept" to "*/*"
            )
            .body("fields *; where id = 1948;")
            .responseString { request, response, result ->
                when (result) {
                    is Result.Success -> {
                        Log.i("API", result.get())
                    }
                    is Result.Failure -> {
                        Log.i("API", result.error.toString())
                    }
                }
            }
        //httpAsync.join()
    }
}
