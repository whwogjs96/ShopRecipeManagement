package com.jj.android.shoprecipemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dataclass.ProcessingListData
import com.jj.android.shoprecipemanagement.viewholder.ProcessingListViewHolder

class MProcessingListAdapter(private val context: Context, var dataList : ArrayList<ProcessingListData>) : RecyclerView.Adapter<ProcessingListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessingListViewHolder {
        return ProcessingListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_processing_material_list, parent, false))
    }

    override fun onBindViewHolder(holder: ProcessingListViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}