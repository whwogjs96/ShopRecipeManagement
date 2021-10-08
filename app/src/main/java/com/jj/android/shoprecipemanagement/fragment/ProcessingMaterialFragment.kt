package com.jj.android.shoprecipemanagement.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.FragmentProcessingMaterialListBinding
import com.jj.android.shoprecipemanagement.interfaceobj.DataRefresh
import com.jj.android.shoprecipemanagement.viewmodel.ProcessMaterialListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProcessingMaterialFragment : CommonFragment<FragmentProcessingMaterialListBinding>(R.layout.fragment_processing_material_list),  View.OnClickListener, DataRefresh {

    private val processingListViewModel : ProcessMaterialListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.processingMaterialListViewModel = processingListViewModel
        dataRefresh()
        binding.processingMaterialAddButton.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        if(processingListViewModel.isDataUpdatable) {
            dataRefresh()
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.processingMaterialAddButton -> {
                Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_processingMaterialProcessFragment)
            }
        }
    }

    override fun dataLoading() {
        CoroutineScope(Dispatchers.Default).launch {
            processingListViewModel.getList()
            withContext(Dispatchers.Main) {
                processingListViewModel.isDataUpdatable = false
                binding.processingMaterialRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }
    override fun dataRefresh() {
        processingListViewModel.clear()
        binding.processingMaterialRecyclerView.adapter?.notifyDataSetChanged()
        super.dataRefresh()
    }
}