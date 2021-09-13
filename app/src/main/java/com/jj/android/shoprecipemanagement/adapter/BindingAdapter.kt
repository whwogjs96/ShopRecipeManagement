package com.jj.android.shoprecipemanagement.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dataclass.ProcessingListData

object BindingAdapter {

    @BindingAdapter("materialListData")
    @JvmStatic
    fun materialListAttach(recyclerView: RecyclerView, costList: ArrayList<MaterialData>) {
        if(recyclerView.adapter == null) recyclerView.adapter = MaterialListAdapter(recyclerView.context, costList)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("processingListData")
    @JvmStatic
    fun processingMaterialListAttach(recyclerView: RecyclerView, dataList : ArrayList<ProcessingListData>) {
        if(recyclerView.adapter == null) recyclerView.adapter = MProcessingListAdapter(recyclerView.context, dataList)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    @BindingAdapter("processingDetailListData")
    @JvmStatic
    fun processingDetailListAttach(recyclerView: RecyclerView, dataDetailList: ArrayList<ProcessingDetailListData>) {
        if(recyclerView.adapter == null) recyclerView.adapter = MaterialListInProcessingAdapter(recyclerView.context, dataDetailList)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}