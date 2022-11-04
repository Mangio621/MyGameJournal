package com.example.my_game_journal

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Exception

/**
 * This class handles the delete confirm dialog in the journal detail page
 */
class JournalDeleteBottomSheet(private val journal: JournalGameInfo, private val onDeleteConfirm: (bottomSheet: BottomSheetDialogFragment) -> Unit) : BottomSheetDialogFragment() {
    private lateinit var bottomSheetViewModel: JournalDeleteBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root: ViewGroup = inflater.inflate(R.layout.fragment_journal_delete_bottom_sheet, container, false) as ViewGroup
        val gameJournalName = root.findViewById<TextView>(R.id.deleteJournalName)
        val yesBtn = root.findViewById<Button>(R.id.yesBtn)
        val noBtn = root.findViewById<Button>(R.id.noBtn)

        // If yes confirmation, call back to the fragment that hosts this dialog where it should be dismissed
        yesBtn.setOnClickListener {
            val journalManager = JournalManager(activity as FragmentActivity)
            onDeleteConfirm.invoke(this)
        }

        noBtn.setOnClickListener {
            dismiss()
        }

        return root
    }

}