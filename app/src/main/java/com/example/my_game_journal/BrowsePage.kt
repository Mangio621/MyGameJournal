package com.example.my_game_journal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.github.kittinunf.result.Result
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * This fragment holds the browse page functionality
 */
class BrowsePage(private val fragmentNavigator: FragmentNavigator) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Create an instance of the Api Manager
        val apiManager: ApiManager = ApiManager()
        // use apiManager to fetch rating desc ordered data and display 200
        apiManager.fetchGameDetails("sort rating desc; where rating > 80; limit: 200") { gameList, result ->
            // Get rid of loading animation
            view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
            if (gameList != null) {
                val browseList = view.findViewById<RecyclerView>(R.id.browseList)
                browseList.adapter = PublicGameListAdapter(gameList) { game ->
                    fragmentNavigator.addFragment(PublicGameDetailPage(fragmentNavigator, game))
                }
                browseList.layoutManager = LinearLayoutManager(context)
            } else if(result is Result.Failure) {
                val msg = "Failed to fetch game data. Make sure you're connected to the internet."
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}