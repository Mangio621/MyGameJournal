package com.example.my_game_journal

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * This class is used for fragments to navigate to other fragments easily
 */
class FragmentNavigator(private val fragmentManager: FragmentManager, private val frameRid: Int, private val bottomNavigationView: BottomNavigationView) {
    /**
     * This function replaces the current fragment in the frame to another
     */
    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(frameRid, fragment)
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    /**
     * This function adds a fragment to display on top of the current fragment
     */
    fun addFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(frameRid, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun getFragmentManager(): FragmentManager {
        return fragmentManager
    }

    fun setBottomNavigationSelectedItem(itemID: Int) {
        bottomNavigationView.selectedItemId = itemID
    }

    fun popStack() {
        fragmentManager.popBackStack()
    }
}