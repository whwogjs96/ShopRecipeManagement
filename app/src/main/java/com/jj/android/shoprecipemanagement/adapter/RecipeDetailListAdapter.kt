package com.jj.android.shoprecipemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.listener.ItemTouchListener
import com.jj.android.shoprecipemanagement.viewholder.MaterialInProcessingListViewHolder

class RecipeDetailListAdapter(
        private val context: Context,
        var dataList: ArrayList<ProcessingDetailListData>
) : RecyclerView.Adapter<MaterialInProcessingListViewHolder>(), ItemTouchListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialInProcessingListViewHolder {
        return MaterialInProcessingListViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(
                                context
                        ), R.layout.item_processing_material_list, parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: MaterialInProcessingListViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onMove(oldPosition: Int, newPosition: Int) {
        val data = dataList[oldPosition]
        dataList.removeAt(oldPosition)
        dataList.add(newPosition, data)
        notifyItemMoved(oldPosition, newPosition)
    }

    override fun onSwipe(position: Int, direction: Int) {
    }
}