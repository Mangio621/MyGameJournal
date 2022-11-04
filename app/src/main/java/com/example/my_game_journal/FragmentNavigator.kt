package com.example.my_game_journal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentNavigator(private val fragmentManager: FragmentManager, private val frameRid: Int) {
    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(frameRid, fragment)
        //fragmentTransaction.add(R.id.frame_layout, fragment)
        if(addToBackStack) {
            fragmentTransaction.addToBackStack(null)
        }
        fragmentTransaction.commit()
    }

    fun addFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(frameRid, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}