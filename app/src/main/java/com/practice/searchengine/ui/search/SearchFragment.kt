package com.practice.searchengine.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practice.searchengine.R
import com.practice.searchengine.ui.arch.MvvmFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : MvvmFragment<SearchContract.Host, SearchContract.ViewModel>() {

    private lateinit var tvSearchedCount: TextView
    private lateinit var tvSearchingCount: TextView
    private lateinit var pbSearch: ProgressBar
    private lateinit var rvSearch: RecyclerView
    private lateinit var btnResume: Button
    private lateinit var btnPause: Button
    private lateinit var btnStop: Button
    private lateinit var fabNewSearch: FloatingActionButton
    private val adapter = SearchListAdapter()

    override fun createModel(): SearchContract.ViewModel {
        val viewModel: SearchViewModel by viewModel()
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        rvSearch.adapter = adapter
        rvSearch.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        subscribeToModelObservables()
        btnPause.setOnClickListener {
            model.pauseSearch()
        }
        btnStop.setOnClickListener {
            model.stopSearch()
        }
        btnResume.setOnClickListener {
            model.resumeSearch()
        }
        fabNewSearch.setOnClickListener {
            if(hasCallBack()){
                callBack!!.backToInitialFragment()
            }
        }
        model.startSearch()
    }

    private fun subscribeToModelObservables() {
        model.getSearchedCountObservable().observe(viewLifecycleOwner, {
            tvSearchedCount.text = it.toString()
        })
        model.getSearchingCountObservable().observe(viewLifecycleOwner, {
            tvSearchingCount.text = it.toString()
        })
        model.getProgressObservable().observe(viewLifecycleOwner, {
            pbSearch.progress = it
        })
        model.getResultsObservable().observe(viewLifecycleOwner, {
            adapter.addItem(it)
        })
        model.getIsErrorObservable().observe(viewLifecycleOwner, {
            if(it){
                makeToast(R.string.searching_error)
            }
        })
        model.getIsCompleteObservable().observe(viewLifecycleOwner, {
            if(it){
                makeToast(R.string.searching_complete)
            }
        })
        model.getSearchingStateObservable().observe(viewLifecycleOwner, {
            when(it) {
                SearchingStateIndication.STATE_SEARCH -> {
                    btnPause.isEnabled = true
                    btnResume.isEnabled = false
                    btnStop.isEnabled = true
                    fabNewSearch.isEnabled = false
                }
                SearchingStateIndication.STATE_PAUSE -> {
                    btnPause.isEnabled = false
                    btnResume.isEnabled = true
                    btnStop.isEnabled = true
                    fabNewSearch.isEnabled = false
                }
                SearchingStateIndication.STATE_STOP -> {
                    btnPause.isEnabled = false
                    btnResume.isEnabled = false
                    btnStop.isEnabled = false
                    fabNewSearch.isEnabled = true
                }
            }
        })
    }

    private fun initViews(view: View) {
        tvSearchedCount = view.findViewById(R.id.tvSearchedCount)
        tvSearchingCount = view.findViewById(R.id.tvSearchingCount)
        pbSearch = view.findViewById(R.id.pbSearching)
        rvSearch = view.findViewById(R.id.rvSearch)
        btnResume = view.findViewById(R.id.btnResumeSearch)
        btnPause = view.findViewById(R.id.btnPauseSearch)
        btnStop = view.findViewById(R.id.btnStopSearch)
        fabNewSearch = view.findViewById(R.id.fabNewSearch)
    }

    private fun makeToast(resId: Int){
        Toast.makeText(activity, resId, Toast.LENGTH_LONG).show()
    }

}