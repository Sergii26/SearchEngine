package com.practice.searchengine.ui.initial

import dagger.Component


@Component(modules = [InitialFragmentModule::class])
interface InitialFragmentComponent {
    fun injectInitialFragment(fragment: InitialFragment)
}