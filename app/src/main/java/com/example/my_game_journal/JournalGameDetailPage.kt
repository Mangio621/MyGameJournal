package com.example.my_game_journal

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.squareup.picasso.Picasso


class JournalGameDetailPage(private val journal: JournalGameInfo) : Fragment() {
    private var newCompletionStatus: Boolean = journal.completed
    private var newJournalContent: String = journal.journal_content
    private var newNoteContent: String = journal.note_content
    private var newRating: Float = journal.rating

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_journal_game_detail_page, container, false)
    }

    private fun setCompletionState(btnFinished: Button, btnUnfinished: Button) {
        btnFinished.setBackgroundColor(ContextCompat.getColor(activity as Activity, R.color.darker_grey))
        btnUnfinished.setBackgroundColor(ContextCompat.getColor(activity as Activity, R.color.darker_grey))
        if(newCompletionStatus) {
            btnFinished.setBackgroundColor(ContextCompat.getColor(activity as Activity, R.color.finished))
        } else {
            btnUnfinished.setBackgroundColor(ContextCompat.getColor(activity as Activity, R.color.unfinished))
        }
    }

    override fun onPause() {
        // Do save via Journal Manager here
        newJournalContent = view?.findViewById<TextView>(R.id.journalContent)?.text.toString()
        newNoteContent = view?.findViewById<TextView>(R.id.journalNoteContent)?.text.toString()
        val journalManager = JournalManager(activity as FragmentActivity)
        val newGameInfo = JournalGameInfo(
            journal.id,
            journal.name,
            journal.cover_url,
            journal.date_added,
            newCompletionStatus,
            newRating,
            newJournalContent,
            newNoteContent
        )
        journalManager.modifyPersistentGameInfo(newGameInfo)
        super.onPause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val journalCover = view.findViewById<ImageView>(R.id.journalGameCover)
        val journalTitle = view.findViewById<TextView>(R.id.journalGameTitle)
        val journalFinishedBtn = view.findViewById<Button>(R.id.finishedBtn)
        val journalUnfinishedBtn = view.findViewById<Button>(R.id.unfinishedBtn)
        val journalRatingBar = view.findViewById<RatingBar>(R.id.journalGameRatingBar)
        val journalRatingText = view.findViewById<TextView>(R.id.journalGameRating)
        val journalContent = view.findViewById<TextView>(R.id.journalContent)
        val journalNotes = view.findViewById<TextView>(R.id.journalNoteContent)
        val journalTimeAdded = view.findViewById<TextView>(R.id.journalTime)

        journalTitle.text = journal.name
        setCompletionState(journalFinishedBtn, journalUnfinishedBtn)
        journalRatingBar.rating = journal.rating
        journalRatingText.text = journal.getRatingToHundred().toString() + "/100"
        journalContent.text = journal.journal_content
        journalNotes.text = journal.note_content
        journalTimeAdded.text = journal.date_added

        if(journal.cover_url != null) {
            Picasso.get()
                .load("https:" + journal.cover_url)
                .into(journalCover)
        } else {
            journalCover.setImageResource(R.drawable.ic_baseline_videogame_asset_24)
        }

        journalFinishedBtn.setOnClickListener {
            newCompletionStatus = true
            setCompletionState(journalFinishedBtn, journalUnfinishedBtn)
        }
        journalUnfinishedBtn.setOnClickListener {
            newCompletionStatus = false
            setCompletionState(journalFinishedBtn, journalUnfinishedBtn)
        }
        journalRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val ratingString = (rating * 10).toInt().toString() + "/100"
            newRating = rating
            journalRatingText.text = ratingString
        }

        super.onViewCreated(view, savedInstanceState)
    }
}