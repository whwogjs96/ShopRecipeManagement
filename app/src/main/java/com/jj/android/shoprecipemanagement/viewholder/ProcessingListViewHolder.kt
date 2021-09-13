package com.jj.android.shoprecipemanagement.viewholder

import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.ItemProcessingMaterialListBinding
import com.jj.android.shoprecipemanagement.dataclass.ProcessingListData

class ProcessingListViewHolder(private val binding: ItemProcessingMaterialListBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProcessingListData) {
        binding.materialName.text = item.name
        binding.unitPricePerGram.text = String.format("%.2f", item.unitPricePerGram)
        binding.totalPrice.text = String.format("%.2f", item.price)
        binding.usage.text = item.usage.toString()
        binding.root.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_mainFragment_to_processingMaterialProcessFragment, bundleOf("id" to item.id))
        }
    }
}