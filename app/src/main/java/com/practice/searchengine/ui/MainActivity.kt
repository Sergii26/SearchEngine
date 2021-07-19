package com.practice.searchengine.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.practice.searchengine.R
import com.practice.searchengine.ui.arch.FragmentContract
import com.practice.searchengine.ui.initial.InitialContract
import com.practice.searchengine.ui.search.SearchContract
import com.practice.searchengine.ui.splash.SplashContract

class MainActivity : AppCompatActivity(), FragmentContract.Host, SearchContract.Host, SplashContract.Host,
InitialContract.Host{
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun openInitialFragment() {
        navController.navigate(R.id.action_splashFragment_to_initialFragment, null, NavOptions.Builder()
            .setPopUpTo(R.id.splashFragment, true)
            .build()
        )
    }

    override fun openSearchFragment() {
        navController.navigate(R.id.action_initialFragment_to_searchFragment, null, NavOptions.Builder()
            .setPopUpTo(R.id.initialFragment, true)
            .build()
        )
    }

    override fun backToInitialFragment() {
        navController.navigate(R.id.action_searchFragment_to_initialFragment, null, NavOptions.Builder()
            .setPopUpTo(R.id.searchFragment, true)
            .build()
        )
    }
}