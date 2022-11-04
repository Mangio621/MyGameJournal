package com.example.my_game_journal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class SearchPage : Fragment() {

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
        val searchText = view.findViewById<TextView>(R.id.searchInput).text

        searchBtn.setOnClickListener {

        }

        super.onViewCreated(view, savedInstanceState)
    }
}