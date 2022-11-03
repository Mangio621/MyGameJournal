package com.example.my_game_journal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [PublicGameDetailPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class PublicGameDetailPage(private val gameInfo: PublicGameInfo) : Fragment() {
    // TODO: Rename and change types of parameters
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
        Picasso.get().load("https:" + gameInfo.getCoverImgUrlSpecificSize("t_720p")).into(gameCover)
        gameTitle.text = gameInfo.name
        gameDescription.text = if(gameInfo.summary != null) gameInfo.summary else "This game has no description"
        gameReleaseDate.text = if(gameInfo.release_dates != null) gameInfo.getReadableReleaseDate() else "This game hasn't been released yet"
        gamePlatforms.text = if(gameInfo.platforms != null) gameInfo.getStringListOfPlatforms() else "No platforms recorded"
        gameRating.text = if(gameInfo.rating != null) gameInfo.getWholeRating().toString() + "/100" else "This game has no reviews yet"
        super.onViewCreated(view, savedInstanceState)
    }
}