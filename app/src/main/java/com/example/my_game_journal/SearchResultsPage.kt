package com.example.my_game_journal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.result.Result

/**
 * This fragment handles the functionality of displaying the game results from the search query
 */
class SearchResultsPage(private val fragmentNavigator: FragmentNavigator, private val query: String) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchQueryLabel = view.findViewById<TextView>(R.id.resultsQuery)
        val searchQueryMessageLabel = view.findViewById<TextView>(R.id.resultsQueryResultMsg)
        searchQueryLabel.text = "For \"${query}\""
        val apiManager = ApiManager()
        // Begin the search with the query retrieved from user input
        apiManager.searchForGameDetails(query) { gameList, result ->
            // Get rid of loading animation when done
            view.findViewById<RelativeLayout>(R.id.loadingPanel).visibility = View.GONE
            if (gameList != null) {
                // Display data to the recycler view
                val resultsList = view.findViewById<RecyclerView>(R.id.resultsList)
                resultsList.adapter = PublicGameListAdapter(gameList) { game ->
                    fragmentNavigator.addFragment(PublicGameDetailPage(fragmentNavigator, game))
                }
                resultsList.layoutManager = LinearLayoutManager(context)
                searchQueryMessageLabel.text = "Returned ${gameList.count()} results"
            } else {
                searchQueryMessageLabel.text = "Returned 0 results"
                if(result is Result.Failure) {
                    val msg = "Failed to fetch game data. Make sure you're connected to the internet."
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}