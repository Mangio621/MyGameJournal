package com.example.my_game_journal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * This fragment handles the functionality for the journal page
 */
class JournalPage(private val fragmentNavigator: FragmentNavigator) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_journal_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val journalManager = JournalManager(activity as FragmentActivity)
        val journalList = journalManager.getPersistentGameList()
        val journalPageMsg = view.findViewById<TextView>(R.id.journalPageMsg)

        // Check that the peristent journal list isn't empty or null
        if(journalList != null && journalList.isNotEmpty()) {
            journalPageMsg.text = "Your added games"
            // Add data to the recycler view
            val journalRecyclerView = view.findViewById<RecyclerView>(R.id.journalList)
                journalRecyclerView.adapter = JournalGameListAdapter(journalList) { journal ->
                    fragmentNavigator.replaceFragment(JournalGameDetailPage(fragmentNavigator, journal), true)
                }
            journalRecyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            journalPageMsg.text = "You haven't added any games to your journal yet. Search for some, or browse for some."
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}