package com.jj.android.shoprecipemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.viewholder.MaterialInProcessingListViewHolder

class MaterialListInProcessingAdapter(private val context: Context, var dataDetailList : ArrayList<ProcessingDetailListData>) : RecyclerView.Adapter<MaterialInProcessingListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialInProcessingListViewHolder {
        return MaterialInProcessingListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_processing_material_list, parent ,false))
    }

    override fun onBindViewHolder(holder: MaterialInProcessingListViewHolder, position: Int) {
        holder.bind(dataDetailList[position])
    }

    override fun getItemCount(): Int {
        return dataDetailList.size
    }
}