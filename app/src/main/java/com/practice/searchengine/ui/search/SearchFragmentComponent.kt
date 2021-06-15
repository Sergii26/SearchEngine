package com.practice.searchengine.ui.search

import dagger.Component


@Component(modules = [SearchFragmentModule::class])
interface SearchFragmentComponent {
    fun injectSearchFragment(fragment: SearchFragment)
}