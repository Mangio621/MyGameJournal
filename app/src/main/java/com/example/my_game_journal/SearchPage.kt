package com.example.my_game_journal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

/**
 * This fragment holds the functionality for the search page
 */
class SearchPage(private val fragmentNavigator: FragmentNavigator) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val searchBtn = view.findViewById<Button>(R.id.searchBtn)

        searchBtn.setOnClickListener {
            val searchText = view.findViewById<TextView>(R.id.searchInput).text
            // Execute search for when the search button is pressed
            fragmentNavigator.replaceFragment(SearchResultsPage(fragmentNavigator, searchText.toString()), true)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}