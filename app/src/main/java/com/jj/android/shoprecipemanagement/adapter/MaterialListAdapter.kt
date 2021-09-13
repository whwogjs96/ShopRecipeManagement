package com.jj.android.shoprecipemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.viewholder.MaterialListViewHolder

class MaterialListAdapter(private val context: Context, var dataList : ArrayList<MaterialData>) : RecyclerView.Adapter<MaterialListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialListViewHolder {
        return MaterialListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_material_list,parent, false))
    }

    override fun onBindViewHolder(holder: MaterialListViewHolder, position: Int) = with(holder) {
        bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}