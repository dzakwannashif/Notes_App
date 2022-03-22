package com.dzakwan.notesapp.utils

import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dzakwan.notesapp.ui.MainActivity
import com.dzakwan.notesapp.R
import com.google.android.material.appbar.MaterialToolbar

object ExtensionFunction {

    fun MaterialToolbar.setActionBar(requireActivity: FragmentActivity) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        (requireActivity as MainActivity).setSupportActionBar(this)
        setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id){
                R.id.updateFragment -> setNavigationIcon(R.drawable.ic_left_arrow)
                R.id.detailFragment -> setNavigationIcon(R.drawable.ic_left_arrow)
                R.id.addFragment -> setNavigationIcon(R.drawable.ic_left_arrow)
            }
        }
    }
}