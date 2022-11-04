package com.example.my_game_journal

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson

const val SHAREDPREF_FILE_NAME = "journal-data"
const val KEY_DATA = "data"

/**
 * This class contains the functions needed to edit/read/modify persistent data via shared preferences
 */
class JournalManager(private val fragmentActivity: FragmentActivity) {
    private val sharedPreferences = fragmentActivity.getSharedPreferences(SHAREDPREF_FILE_NAME, Context.MODE_PRIVATE)
    private val prefEditor = sharedPreferences.edit()

    /**
     * This function simply gets the json string from the shared preferences and maps the data into a list of journals
     */
    fun getPersistentGameList(): List<JournalGameInfo>? {
        var list: List<JournalGameInfo>? = null
        val serializedObject = sharedPreferences.getString(KEY_DATA, null)
        if (serializedObject != null && serializedObject != "null") {
            val gson = Gson()
            // Map the json data into an array of JournalGameInfo objects
            list = gson.fromJson(serializedObject, Array<JournalGameInfo>::class.java).toList()
        }
        return list
    }

    /**
     * This function simply replaces the json string with an update json string
     */
    private fun writeListToPersistentData(mutableList: MutableList<JournalGameInfo>) {
        val list = mutableList.toList()
        val gson = Gson()
        val json = gson.toJson(list)
        prefEditor.putString(KEY_DATA, json)
        prefEditor.commit()
    }

    /**
     * This function gets a mutableList version of a non add-able list
     */
    private fun getMutableListOf(list: List<JournalGameInfo>?): MutableList<JournalGameInfo> {
        var mutableList: MutableList<JournalGameInfo> = mutableListOf()
        if(list != null) {
            mutableList = list.toMutableList()
        }
        return mutableList
    }

    /**
     * This function grabs the index in the array, a game journal of which its ID matches another ID
     */
    private fun getIndexOfJournalMatchingID(id: Int, existingJournalsList: MutableList<JournalGameInfo>): Int {
        val index = existingJournalsList.indexOfFirst {
            it.id == id
        }
        return index
    }

    /**
     * This function simply rewrites the json string in the shared preferences with a modified game journal
     */
    fun modifyPersistentGameInfo(newGameInfo: JournalGameInfo) {
        val mutableList = getMutableListOf(getPersistentGameList())
        mutableList[getIndexOfJournalMatchingID(newGameInfo.id, mutableList)] = newGameInfo
        writeListToPersistentData(mutableList)
    }

    /**
     * This function rewrites the json string with a new list of game journals including a new added journal
     */
    fun addToPersistentGameList(gameInfo: JournalGameInfo) {
        val mutableList = getMutableListOf(getPersistentGameList())
        mutableList.add(gameInfo)
        writeListToPersistentData(mutableList)
    }

    /**
     * This function rewrites the json string with a new list of game journals excluding a journal that has been added
     */
    fun deletePersistentGameInfo(gameInfoToDelete: JournalGameInfo) {
        val mutableList = getMutableListOf(getPersistentGameList())
        val indexOfGameToDelete = getIndexOfJournalMatchingID(gameInfoToDelete.id, mutableList)
        mutableList.removeAt(indexOfGameToDelete)
        writeListToPersistentData(mutableList)
    }

    /**
     * This function returns true/false based on whether an instance of a gameID is found in the shared preferences
     */
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