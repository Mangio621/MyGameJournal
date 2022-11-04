package com.example.my_game_journal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class PublicGameListAdapter(private val gameList: List<PublicGameInfo>, val gameClicked: (gameID: PublicGameInfo) -> Unit): RecyclerView.Adapter<PublicGameListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.public_game_list_item, parent, false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = gameList.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = gameList[position]
        holder.bind(game)
    }

    inner class ViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        private val itemTouchZone = view.findViewById<LinearLayout>(R.id.gameInfoItem)
        private val nameText = view.findViewById<TextView>(R.id.gameInfoName)
        private val coverImg = view.findViewById<ImageView>(R.id.gameInfoCover)
        private val releaseText = view.findViewById<TextView>(R.id.gameInfoRelease)
        private val ratingText = view.findViewById<TextView>(R.id.gameInfoRating)

        fun bind(game: PublicGameInfo) {
            nameText.text = game.getShortenedName()
            if(game.rating != null) {
                ratingText.text = "Critic Score: ${game.getWholeRating()}/100"
            } else {
                ratingText.text = "No Reviews Yet"
            }
            if(game.release_dates != null) {
                releaseText.text = "Release Date: ${game.getReadableReleaseDate()}"
            } else {
                releaseText.text = "Not Released Yet"
            }
            if(game.cover != null) {
                Picasso.get()
                    .load("https:" + game.getCoverImgUrlSpecificSize("t_cover_big"))
                    .into(coverImg)
            } else {
                coverImg.setImageResource(R.drawable.ic_baseline_videogame_asset_24)
            }
            itemTouchZone.setOnClickListener {
                gameClicked.invoke(game)
            }
        }
    }
}