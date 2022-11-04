package com.example.my_game_journal

import android.util.Log
import java.text.SimpleDateFormat
import kotlin.math.round

data class PublicGameInfo(
    val id: Int? = null,
    val cover: PublicGameInfoCover? = null,
    val name: String? = null,
    val platforms: List<PublicGameInfoPlatform>? = null,
    val rating: Double? = null,
    val release_dates: List<PublicGameInfoReleaseDate>? = null,
    val summary: String? = null
) {
    inner class PublicGameInfoCover(
        val id: Int,
        val url: String
    )

    inner class PublicGameInfoPlatform(
        val id: Int,
        val name: String
    )

    inner class PublicGameInfoReleaseDate(
        val id: Int,
        val date: Int
    )

    fun getCoverImgUrlSpecificSize(size: String): String {
        var newUrl = ""
        if(cover != null) {
            newUrl = cover.url.replace("t_thumb", size)
        }
        return newUrl;
    }

    fun getShortenedName(): String {
        var newName = name.toString()
        val maxLength = 20;
        if(newName.length >= maxLength) {
            newName = newName.removeRange(maxLength, newName.length)
            newName += "..."
        }
        return newName
    }

    fun getReadableReleaseDate(): String {
        var readableDate: String = ""
        if(release_dates != null) {
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val date = java.util.Date(release_dates[0].date.toLong() * 1000)
            readableDate = formatter.format(date)
        }
        return readableDate
    }

    fun getWholeRating(): Int {
        var newRating: Int = 0
        if(rating != null) {
            newRating = round(rating).toInt()
        }
        return newRating
    }

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