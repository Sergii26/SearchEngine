package com.practice.searchengine.ui.splash


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.practice.searchengine.R
import com.practice.searchengine.ui.arch.MvvmFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashFragment : MvvmFragment<SplashContract.Host, SplashContract.ViewModel>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getIsReady().observe(viewLifecycleOwner, Observer { aBoolean: Boolean? ->
            if (aBoolean!! && hasCallBack()) {
                callBack!!.openInitialFragment()
            }
        })

        model.startTimer()
    }

    override fun createModel(): SplashContract.ViewModel {
        val viewModel: SplashViewModel by viewModel()
        return viewModel
    }

}