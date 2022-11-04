package com.example.my_game_journal

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * This fragmant handles the functionality of displaying the detail view of a public game retrieved via the IGDB game endpoint
 */
class PublicGameDetailPage(private val fragmentNavigator: FragmentNavigator, private val gameInfo: PublicGameInfo) : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_public_game_detail_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val gameCover = view.findViewById<ImageView>(R.id.publicGameCover)
        val gameTitle = view.findViewById<TextView>(R.id.publicGameTitle)
        val gameDescription = view.findViewById<TextView>(R.id.publicGameDescription)
        val gameReleaseDate = view.findViewById<TextView>(R.id.publicGameReleaseDate)
        val gamePlatforms = view.findViewById<TextView>(R.id.publicGamePlatforms)
        val gameRating = view.findViewById<TextView>(R.id.publicGameRating)
        val addGameBtn = view.findViewById<Button>(R.id.addGameBtn)

        // Asynchronously retrieve jpg content of the games cover url and set it to the gameCover ImageView
        if(gameInfo.cover != null) {
            Picasso.get()
                .load("https:" + gameInfo.getCoverImgUrlSpecificSize("t_720p"))
                .into(gameCover)
        } else {
            // If the url doesn't exist, which for some games is the case, load in a default icon asset
            gameCover.setImageResource(R.drawable.ic_baseline_videogame_asset_24)
        }
        gameTitle.text = gameInfo.name
        gameDescription.text = if(gameInfo.summary != null) gameInfo.summary else "This game has no description"
        gameReleaseDate.text = if(gameInfo.release_dates != null) gameInfo.getReadableReleaseDate() else "This game hasn't been released yet"
        gamePlatforms.text = if(gameInfo.platforms != null) gameInfo.getStringListOfPlatforms() else "No platforms recorded"
        gameRating.text = if(gameInfo.rating != null) gameInfo.getWholeRating().toString() + "/100" else "This game has no reviews yet"
        val journalManager = JournalManager(activity as FragmentActivity)
        // Check whether the game exists in the shared preferences. If it does, don't let the user re-add it.
        if(!journalManager.gamePersistentlyExists(gameInfo.id as Int)) {
            addGameBtn.setOnClickListener {
                val timeFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
                // Get the current date to log when this game was added to the journal
                val currentDate: String = timeFormatter.format(Date())

                // Add a new game journal instance persistently for the user to later edit
                journalManager.addToPersistentGameList(
                    JournalGameInfo(
                        gameInfo.id ?: 0,
                        gameInfo.name ?: "",
                        gameInfo.getCoverImgUrlSpecificSize("t_720p"),
                        currentDate
                    )
                )
                fragmentNavigator.setBottomNavigationSelectedItem(R.id.journal)
                // Navigate to the journal page
                fragmentNavigator.replaceFragment(JournalPage(fragmentNavigator), true)
            }
        } else {
            addGameBtn.background.setTint(ContextCompat.getColor(activity as Activity, R.color.darker_grey))
            addGameBtn.text = "Game already added. Go To Your Journals?"
            addGameBtn.setOnClickListener {
                fragmentNavigator.setBottomNavigationSelectedItem(R.id.journal)
                fragmentNavigator.replaceFragment(JournalPage(fragmentNavigator), true)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}