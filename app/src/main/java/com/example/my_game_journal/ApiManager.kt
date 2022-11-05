package com.example.my_game_journal

import android.util.JsonReader
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject
import java.io.StringReader
import java.lang.reflect.Type

/**
 * This class holds functions that help make API POST calls with the help of the package "Fuel" for async post requests
 */
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

    /**
     * This function fetches a list of games from the games endpoint without searching.
     */
    fun fetchGameDetails(query: String, callback: (gameList: List<PublicGameInfo>?, postResult: Result<String, FuelError>) -> Unit) {
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
                            // Maps json data to a list of objects
                            val list =  gson.fromJson(data, Array<PublicGameInfo>::class.java).toList()
                            callback.invoke(list, result)
                        }
                        is Result.Failure -> {
                            val error = result.error.toString()
                            callback.invoke(null, result)
                        }
                    }
                }
        }
    }

    /**
     * This function fetches a list of games from the search endpoint and uses the API's search query system
     */
    fun searchForGameDetails(query: String, callback: (gameList: List<PublicGameInfo>?, postResult: Result<String, FuelError>) -> Unit) {
        val url = "https://api.igdb.com/v4/search"
        getAccessToken { auth ->
            val httpAsync = url.httpPost()
                .header(
                    "Client-ID" to "lo0phw9dgi0gei5pj5w9tnlooqgp5f",
                    "Authorization" to "Bearer ${auth.access_token}"
                )
                .body("fields game.id, game.name, game.summary, game.cover.url, game.rating, game.release_dates.date, game.platforms.name; search \"${query}\"; limit 200;")
                .responseString {request, response, result ->
                    when (result) {
                        is Result.Success -> {
                            val data = result.get()
                            val gson = Gson()
                            // Maps json data to a list of objectss
                            val searchList =  gson.fromJson(data, Array<SearchedPublicGameInfo>::class.java).toList()
                            val mutableListOfGames = mutableListOf<PublicGameInfo>()
                            for(searchResult in searchList) {
                                if(searchResult.game != null) {
                                    mutableListOfGames.add(searchResult.game)
                                }
                            }
                            // Sort List as search endpoint doesn't support sort queries
                            val sortedMutableListOfGames = mutableListOfGames.sortedWith(compareByDescending{ it.rating })
                            callback.invoke(sortedMutableListOfGames, result)
                        }
                        is Result.Failure -> {
                            val error = result.error.toString()
                            callback.invoke(null, result)
                        }
                    }
                }
        }
    }
}