package com.jj.android.shoprecipemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dataclass.RecipeListData
import com.jj.android.shoprecipemanagement.viewholder.RecipeListViewHolder

class RecipeListAdapter(private val context: Context,
var dataList: ArrayList<RecipeListData>) : RecyclerView.Adapter<RecipeListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        return RecipeListViewHolder(DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_recipe_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}