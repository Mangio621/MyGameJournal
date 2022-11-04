package com.example.my_game_journal

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson

const val SHAREDPREF_FILE_NAME = "journal-data"
const val KEY_DATA = "data"
class JournalManager(private val fragmentActivity: FragmentActivity) {
    private val sharedPreferences = fragmentActivity.getSharedPreferences(SHAREDPREF_FILE_NAME, Context.MODE_PRIVATE)
    private val prefEditor = sharedPreferences.edit()

    fun getPersistentGameList(): List<JournalGameInfo>? {
        var list: List<JournalGameInfo>? = null
        val serializedObject = sharedPreferences.getString(KEY_DATA, null)
        if (serializedObject != null && serializedObject != "null") {
            val gson = Gson()
            list = gson.fromJson(serializedObject, Array<JournalGameInfo>::class.java).toList()
        }
        return list
    }

    fun addToPersistentGameList(gameInfo: JournalGameInfo) {
        var list: List<JournalGameInfo>? = getPersistentGameList()
        var mutableList: MutableList<JournalGameInfo> = mutableListOf()
        if(list != null) {
            mutableList = list.toMutableList()
        }
        mutableList.add(gameInfo)
        list = mutableList.toList()
        val gson = Gson()
        val json = gson.toJson(list)
        Log.i("API", json)
        prefEditor.putString(KEY_DATA, json)
        prefEditor.commit()
    }
}