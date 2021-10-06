package com.jj.android.shoprecipemanagement.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.databinding.ItemProcessingMaterialListBinding
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dialog.MaterialSelectDialog
import com.jj.android.shoprecipemanagement.eventbus.ProcessingMaterialModifyEvent
import com.jj.android.shoprecipemanagement.result.ProcessingMaterialDialogResult
import org.greenrobot.eventbus.EventBus

class MaterialInProcessingListViewHolder(private val binding: ItemProcessingMaterialListBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

    var item :ProcessingDetailListData? = null
    fun bind(dataDetail : ProcessingDetailListData) {
        item = dataDetail
        with(binding) {
            materialName.text = dataDetail.materialName
            unitPricePerGram.text = String.format("%.2f", dataDetail.unitPricePerGram)
            usage.text = dataDetail.usage.toString()
            totalPrice.text = String.format("%.2f", dataDetail.totalPrice)
        }
        binding.root.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.root -> {
                val dialog = MaterialSelectDialog(v.context, 2, dataDetail = item)
                dialog.setResult(object : ProcessingMaterialDialogResult {
                    override fun finish(dataDetail: ProcessingDetailListData) {
                        EventBus.getDefault().post(ProcessingMaterialModifyEvent(dataDetail, adapterPosition))
                    }
                })
                dialog.show()
            }
        }
    }
}