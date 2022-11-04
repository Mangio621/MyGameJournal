package com.example.my_game_journal

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso

/**
 * This fragment holds the functionality for the journal detail page (launched from the journal page)
 * Shows data for all saved game journals and allows editing of journals
 */
class JournalGameDetailPage(private val fragmentNavigator: FragmentNavigator, private val journal: JournalGameInfo) : Fragment() {
    // Store new data that we would want to save to persistent data
    private var newCompletionStatus: Boolean = journal.completed
    private var newJournalContent: String = journal.journal_content
    private var newNoteContent: String = journal.note_content
    private var newRating: Float = journal.rating

    // Used for when if a a journal is in the process of deleting itself, don't save the data on pause
    private var deleting: Boolean = false

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

    /**
     * This function handles the "toggle" look of the finished and unfinished buttons
     */
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
        // When fragment is paused, save modified data persistently if the journal is not being deleted
        if(!deleting) {
            // Do save via Journal Manager here
            newJournalContent = view?.findViewById<TextView>(R.id.journalContent)?.text.toString()
            newNoteContent = view?.findViewById<TextView>(R.id.journalNoteContent)?.text.toString()
            val journalManager = JournalManager(activity as FragmentActivity)
            // We don't need to save the id, name etc as they aren't editable
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
            // Modify the persistent data for this game journal
            journalManager.modifyPersistentGameInfo(newGameInfo)
        }
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
        val journalGoBackBtn = view.findViewById<Button>(R.id.journalGoBackBtn)
        val journalDeleteBtn = view.findViewById<Button>(R.id.journalDeleteBtn)

        // Update data on creation
        journalTitle.text = journal.name
        setCompletionState(journalFinishedBtn, journalUnfinishedBtn)
        journalRatingBar.rating = journal.rating
        journalRatingText.text = journal.getRatingToHundred().toString() + "/100"
        journalContent.text = journal.journal_content
        journalNotes.text = journal.note_content
        journalTimeAdded.text = journal.date_added

        // Asynchronously set the journal cover to the jpg contents of the game data's cover url.
        if(journal.cover_url != null) {
            Picasso.get()
                .load("https:" + journal.cover_url)
                .into(journalCover)
        } else {
            // If the cover url doesn't exist, then simply use a default icon to replace it
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

        // When rating bar is tapped on, update the rating text
        journalRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val ratingString = (rating * 10).toInt().toString() + "/100"
            newRating = rating
            journalRatingText.text = ratingString
        }

        journalGoBackBtn.setOnClickListener {
            fragmentNavigator.popStack()
            fragmentNavigator.replaceFragment(JournalPage(fragmentNavigator))
        }

        journalDeleteBtn.setOnClickListener {
            val bottomSheet = JournalDeleteBottomSheet(journal) { // Here, the delete has been confirmed from the dialog
                it.dismiss()
                val journalManager = JournalManager(activity as FragmentActivity)
                journalManager.deletePersistentGameInfo(journal)
                // Don't save when this navigates back to the journal page fragment as this data no longer should be saved
                deleting = true
                fragmentNavigator.popStack()
                fragmentNavigator.replaceFragment(JournalPage(fragmentNavigator))
            }
            // Show the fragment when delete button is pressed
            bottomSheet.show(fragmentNavigator.getFragmentManager(), "deleteBottomSheet")
        }
        
        journalContent.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus)
                view.findViewById<TextInputLayout>(R.id.journalContentLayout).setHintTextAppearance(R.style.hint)
        }
        journalNotes.setOnFocusChangeListener { y, hasFocus ->
            if(hasFocus)
                view.findViewById<TextInputLayout>(R.id.journalNotesLayout).setHintTextAppearance(R.style.hint)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}