package com.example.my_game_journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class JournalGameListAdapter(private val journalList: List<JournalGameInfo>, val journalClicked: (journal: JournalGameInfo) -> Unit): RecyclerView.Adapter<JournalGameListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.journal_game_list_item, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = journalList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = journalList[position]
        holder.bind(game)
    }

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val journalName = view.findViewById<TextView>(R.id.journalInfoName)
        private val journalCover = view.findViewById<ImageView>(R.id.journalInfoCover)

        fun bind(journal: JournalGameInfo) {
            journalName.text = journal.name

            if(journal.cover_url != null) {
                Picasso.get()
                    .load("https:" + journal.cover_url)
                    .into(journalCover)
            } else {
                journalCover.setImageResource(R.drawable.ic_baseline_videogame_asset_24)
            }
        }
    }
}