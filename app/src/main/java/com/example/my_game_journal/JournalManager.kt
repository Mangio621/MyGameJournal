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

    private fun writeListToPersistentData(mutableList: MutableList<JournalGameInfo>) {
        val list = mutableList.toList()
        val gson = Gson()
        val json = gson.toJson(list)
        prefEditor.putString(KEY_DATA, json)
        prefEditor.commit()
    }

    private fun getMutableListOf(list: List<JournalGameInfo>?): MutableList<JournalGameInfo> {
        var mutableList: MutableList<JournalGameInfo> = mutableListOf()
        if(list != null) {
            mutableList = list.toMutableList()
        }
        return mutableList
    }

    fun modifyPersistentGameInfo(newGameInfo: JournalGameInfo) {
        val mutableList = getMutableListOf(getPersistentGameList())
        val indexOfModifedGame = mutableList.indexOfFirst {
            it.id == newGameInfo.id
        }
        mutableList[indexOfModifedGame] = newGameInfo
        writeListToPersistentData(mutableList)
    }

    fun addToPersistentGameList(gameInfo: JournalGameInfo) {
        val mutableList = getMutableListOf(getPersistentGameList())
        mutableList.add(gameInfo)
        writeListToPersistentData(mutableList)
    }

    fun gamePersistentlyExists(gameID: Int): Boolean {
        var found = false
        if(getPersistentGameList() != null) {
            for(game in getPersistentGameList() as List<JournalGameInfo>) {
                if(game.id == gameID) {
                    found = true
                    break
                }
            }
        }
        return found
    }
}