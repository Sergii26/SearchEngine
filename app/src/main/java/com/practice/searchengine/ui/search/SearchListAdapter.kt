package com.practice.searchengine.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.searchengine.R
import com.practice.searchengine.databinding.ItemPageBinding
import com.practice.searchengine.model.bfs_worker.PageResult

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.ResultViewHolder>(){

    private val itemList = ArrayList<PageResult>()

    fun addItem(item: PageResult){
        itemList.add(item)
        notifyDataSetChanged()
    }

    fun getItemList(): List<PageResult>{
        return itemList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemPageBinding = DataBindingUtil.inflate(inflater, R.layout.item_page, parent, false)
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bindView(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ResultViewHolder(var binding: ItemPageBinding): RecyclerView.ViewHolder(binding.root){
        fun bindView(result: PageResult) {
            binding.pageResult = result
            binding.executePendingBindings()
        }
    }

}