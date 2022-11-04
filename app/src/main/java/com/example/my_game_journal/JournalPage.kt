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

        if(journalList != null) {
            val journalRecyclerView = view.findViewById<RecyclerView>(R.id.journalList)
                journalRecyclerView.adapter = JournalGameListAdapter(journalList) { journal ->
                    fragmentNavigator.replaceFragment(JournalGameDetailPage(journal), true)
                }
            journalRecyclerView.layoutManager = LinearLayoutManager(context)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}