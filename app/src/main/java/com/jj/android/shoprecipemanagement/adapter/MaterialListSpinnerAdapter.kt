package com.jj.android.shoprecipemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.ItemSpinnerMaterialArrayBinding
import com.jj.android.shoprecipemanagement.dto.MaterialData

class MaterialListSpinnerAdapter(private val context: Context, private val dataList: ArrayList<MaterialData>) : BaseAdapter()  {


    private val inflater  = LayoutInflater.from(context)
    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var binding : ItemSpinnerMaterialArrayBinding?
        if(view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.item_spinner_material_array, parent, false)
            view = binding.root
            binding.materialNameTextView.text = dataList[position].name
        }
        return view
    }
}