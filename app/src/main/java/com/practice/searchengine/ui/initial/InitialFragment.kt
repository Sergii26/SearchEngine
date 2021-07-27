package com.practice.searchengine.ui.initial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.practice.searchengine.R
import com.practice.searchengine.ui.arch.MvvmFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class InitialFragment : MvvmFragment<InitialContract.Host, InitialContract.ViewModel>() {

    private lateinit var etWebsite: EditText
    private lateinit var etSearchText: EditText
    private lateinit var etThreadsCount: EditText
    private lateinit var etPagesLimit: EditText
    private lateinit var btnStart: Button

    override fun createModel(): InitialContract.ViewModel {
        val viewModel: InitialViewModel by viewModel()
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_initial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        subscribeToModelObservables()
        btnStart.setOnClickListener {
            model.checkValues(
                etWebsite.text.toString(),
                etSearchText.text.toString(),
                etThreadsCount.text.toString(),
                etPagesLimit.text.toString()
            )
        }
    }

    private fun initViews(view: View){
        etWebsite = view.findViewById(R.id.etWebsite)
        etSearchText = view.findViewById(R.id.etSearchText)
        etThreadsCount = view.findViewById(R.id.etThreadsCount)
        etPagesLimit = view.findViewById(R.id.etPagesLimit)
        btnStart = view.findViewById(R.id.btnStart)
    }

    private fun subscribeToModelObservables(){
        model.getIncorrectWebsiteObservable().observe(viewLifecycleOwner, {
            etWebsite.error = getString(it)
        })
        model.getIncorrectSearchTextObservable().observe(viewLifecycleOwner, {
            etSearchText.error = getString(it)
        })
        model.getIncorrectThreadsCountObservable().observe(viewLifecycleOwner, {
            etThreadsCount.error = getString(it)
        })
        model.getIncorrectPagesLimitObservable().observe(viewLifecycleOwner, {
            etPagesLimit.error = getString(it)
        })
        model.getIsCheckedValues().observe(viewLifecycleOwner, {
            if(it && hasCallBack()){
                callBack!!.openSearchFragment()
            }
        })
    }

}