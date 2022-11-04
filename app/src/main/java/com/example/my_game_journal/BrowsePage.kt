package com.example.my_game_journal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [BrowsePage.newInstance] factory method to
 * create an instance of this fragment.
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
        apiManager.fetchGameDetails("limit: 20") { gameList ->
            if(gameList != null) {
                val browseList = view.findViewById<RecyclerView>(R.id.browseList)
                browseList.adapter = PublicGameListAdapter(gameList) { game ->
                    fragmentNavigator.addFragment(PublicGameDetailPage(fragmentNavigator, game))
                }
                browseList.layoutManager = LinearLayoutManager(context)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}