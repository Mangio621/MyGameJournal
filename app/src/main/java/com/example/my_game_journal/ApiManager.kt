package com.example.my_game_journal

import android.util.JsonReader
import android.util.Log
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.io.StringReader

class ApiManager {
    /**
     * This function grabs the authorization token and has a callback function of which carries the AccessTokenInfo object
     */
    private fun getAccessToken(callback: (auth: AccessTokenInfo) -> Unit) {
        val url = "https://id.twitch.tv/oauth2/token?client_id=lo0phw9dgi0gei5pj5w9tnlooqgp5f&client_secret=w4mk3sm0xn151umyosja37qhj19sku&grant_type=client_credentials"
        val async = url.httpPost().responseString { request, response, result ->
            when (result) {
                is Result.Success -> {
                    val data = result.get()
                    val gson = Gson()
                    val authorization = gson.fromJson(data, AccessTokenInfo::class.java)
                    callback.invoke(authorization)
                }
                is Result.Failure -> {
                    val error = result.error.toString()
                    callback.invoke(AccessTokenInfo("fail", 0, "fail"))
                }
            }
        }
    }

    fun fetchGameDetails(query: String, callback: (gameList: List<PublicGameInfo>?) -> Unit) {
        val url = "https://api.igdb.com/v4/games"
        getAccessToken { auth ->
            val httpAsync = url.httpPost()
                .header(
                    "Client-ID" to "lo0phw9dgi0gei5pj5w9tnlooqgp5f",
                    "Authorization" to "Bearer ${auth.access_token}",
                )
                .body("fields id, name, summary, cover.url, rating, release_dates.date, platforms.name; ${query};")
                .responseString { request, response, result ->
                    when (result) {
                        is Result.Success -> {
                            val data = result.get()
                            val gson = Gson()
                            val gameInfoList: List<PublicGameInfo> = gson.fromJson(data, Array<PublicGameInfo>::class.java).toList()
                            callback.invoke(gameInfoList)
                        }
                        is Result.Failure -> {
                            val error = result.error.toString()
                            callback.invoke(null)
                        }
                    }
                }
        }
    }
}