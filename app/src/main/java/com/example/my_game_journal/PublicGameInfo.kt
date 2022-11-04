package com.example.my_game_journal

import android.util.Log
import java.text.SimpleDateFormat
import kotlin.math.round

/**
 * This class holds data from json of a searched game
 */
data class SearchedPublicGameInfo(
    val id: Int? = null,
    val game: PublicGameInfo? = null
)

/**
 * This class holds the data from json of a game object in the IGDP game endpoint
 */
data class PublicGameInfo(
    val id: Int? = null,
    val cover: PublicGameInfoCover? = null,
    val name: String? = null,
    val platforms: List<PublicGameInfoPlatform>? = null,
    val rating: Double? = null,
    val release_dates: List<PublicGameInfoReleaseDate>? = null,
    val summary: String? = null
) {
    // Model for the cover field
    inner class PublicGameInfoCover(
        val id: Int,
        val url: String
    )
    // Model for the platform field
    inner class PublicGameInfoPlatform(
        val id: Int,
        val name: String
    )
    // Model for the release date field
    inner class PublicGameInfoReleaseDate(
        val id: Int,
        val date: Int
    )

    // Get a new img jpb size by changing the url for higher or lower res images
    fun getCoverImgUrlSpecificSize(size: String): String {
        var newUrl = ""
        if(cover != null) {
            newUrl = cover.url.replace("t_thumb", size)
        }
        return newUrl;
    }

    // If name is too long, remove a bit of the end and add a ...
    fun getShortenedName(): String {
        var newName = name.toString()
        val maxLength = 20;
        if(newName.length >= maxLength) {
            newName = newName.removeRange(maxLength, newName.length)
            newName += "..."
        }
        return newName
    }

    // Simply converts a unix time stamp to human readable date
    fun getReadableReleaseDate(): String {
        var readableDate: String = ""
        if(release_dates != null) {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val date = java.util.Date(release_dates[0].date.toLong() * 1000)
            readableDate = formatter.format(date)
        }
        return readableDate
    }

    // Simply rounds the rating double to an int. e.g 96.222111 to 96
    fun getWholeRating(): Int {
        var newRating: Int = 0
        if(rating != null) {
            newRating = round(rating).toInt()
        }
        return newRating
    }

    // Simply returns a string containing each platform on a new line
    fun getStringListOfPlatforms(): String {
        var stringList: String = ""
        if(platforms != null) {
            for(platform in platforms) {
                stringList += "- ${platform.name}\n"
            }
        }
        return stringList
    }
}