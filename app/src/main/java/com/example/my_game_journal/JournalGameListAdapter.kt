package com.example.my_game_journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        private val journalRating = view.findViewById<TextView>(R.id.journalInfoRating)
        private val journalCompletion = view.findViewById<TextView>(R.id.journalInfoCompletion)
        private val journalTouchZone = view.findViewById<LinearLayout>(R.id.journalInfoItem)
        private val timeText = view.findViewById<TextView>(R.id.journalInfoTime)

        fun bind(journal: JournalGameInfo) {
            journalName.text = journal.name
            journalRating.text = "${journal.getRatingToHundred()}/100"
            journalCompletion.text = if(journal.completed) "FINISHED" else "UNFINISHED"
            timeText.text = journal.date_added.split(" ")[0]
            journalCompletion.background.setTint(ContextCompat.getColor(view.context,
                if(journal.completed) R.color.finished else R.color.unfinished
            ))
            if(journal.cover_url != null) {
                Picasso.get()
                    .load("https:" + journal.cover_url)
                    .into(journalCover)
            } else {
                journalCover.setImageResource(R.drawable.ic_baseline_videogame_asset_24)
            }

            journalTouchZone.setOnClickListener {
                journalClicked.invoke(journal)
            }
        }
    }
}