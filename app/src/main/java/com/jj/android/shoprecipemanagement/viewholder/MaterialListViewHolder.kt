package com.jj.android.shoprecipemanagement.viewholder

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.databinding.ItemMaterialListBinding
import com.jj.android.shoprecipemanagement.dialog.MaterialPrecessDialog
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.eventbus.MaterialModifyEvent
import com.jj.android.shoprecipemanagement.result.MaterialDialogResult
import com.jj.android.shoprecipemanagement.viewmodel.MaterialListViewModel
import org.greenrobot.eventbus.EventBus

class MaterialListViewHolder(private val binding: ItemMaterialListBinding) : RecyclerView.ViewHolder(binding.root){

    fun bind(item: MaterialData) {
        binding.apply {
            materialName.text = item.name
            unitPrice.text = item.unitPrice.toString()
            weight.text = item.weight.toString()
            unitPricePerGram.text = item.unitPricePerGram.toString()
            root.setOnClickListener {
                val dialog = MaterialPrecessDialog(binding.root.context, 2, item)
                dialog.setResult(object : MaterialDialogResult {
                    override fun finish(data: MaterialData) {
                        EventBus.getDefault().post(MaterialModifyEvent(data, adapterPosition))
                    }
                })
                dialog.show()
            }
        }
    }
}