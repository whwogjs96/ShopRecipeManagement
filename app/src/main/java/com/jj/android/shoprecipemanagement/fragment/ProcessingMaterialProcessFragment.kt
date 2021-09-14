package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.FragmentProcessingMaterialProcessBinding
import com.jj.android.shoprecipemanagement.dialog.ProcessingMaterialProcessDialog
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.eventbus.ProcessingMaterialModifyEvent
import com.jj.android.shoprecipemanagement.result.ProcessingMaterialDialogResult
import com.jj.android.shoprecipemanagement.viewmodel.ProcessingDetailListViewModel
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ProcessingMaterialProcessFragment : CommonFragment<FragmentProcessingMaterialProcessBinding>(R.layout.fragment_processing_material_process), View.OnClickListener {

    val processingDetailListViewModel : ProcessingDetailListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.processingMaterialDetailViewModel = processingDetailListViewModel
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processingDetailListViewModel.initDAO(context?:return)
        processingDetailListViewModel.processingMaterialId = arguments?.getInt("id", 0) ?:0
        if(processingDetailListViewModel.processingMaterialId != 0) {
            CoroutineScope(Dispatchers.Default).launch {
                val data = processingDetailListViewModel.getDataById()
                processingDetailListViewModel.getDetailDataList(processingDetailListViewModel.processingMaterialId)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.processingMDetailRecyclerView.adapter?.notifyDataSetChanged()
                    binding.processingMaterialNameView.setText(data?.name?:"")
                }
            }
            binding.addButton.text = getString(R.string.modify)
            binding.processingMaterialNameLayout.hint = getString(R.string.modifyMaterialName)
        } else {
            binding.addButton.text = getString(R.string.add)
            binding.processingMaterialNameLayout.hint = getString(R.string.addMaterialName)
        }
        binding.cancelButton.setOnClickListener(this)
        binding.materialAddButton.setOnClickListener(this)
        binding.addButton.setOnClickListener(this)
        binding.deleteButton.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v) {
            binding.cancelButton -> {
                Navigation.findNavController(v).popBackStack()
            }
            binding.materialAddButton -> {
                val dialog = ProcessingMaterialProcessDialog(binding.root.context, 1)
                dialog.setResult(object : ProcessingMaterialDialogResult {
                    override fun finish(dataDetail: ProcessingDetailListData) {
                        CoroutineScope(Dispatchers.Default).launch {
                            val data = processingDetailListViewModel.getDataById()
                            CoroutineScope(Dispatchers.Main).launch {
                                Log.e("뭐가 문제지?", dataDetail.toString())
                                if(data != null && data.name == dataDetail.materialName && dataDetail.type == 2) {
                                    StyleableToast.makeText(context?: return@launch, "현재 재료는 재료로 추가할 수 없습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                                } else {
                                    processingDetailListViewModel.dataAdd(dataDetail)
                                }
                                binding.processingMDetailRecyclerView.adapter?.notifyDataSetChanged()
                            }
                        }
                    }
                })
                dialog.show()
            }
            binding.addButton -> {
                var processingMaterialName = binding.processingMaterialNameView.text
                processingDetailListViewModel.processingDataSave(context?:return, processingMaterialName.toString()) {
                    StyleableToast.makeText(context?:return@processingDataSave, "재료를 추가했습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
                    Navigation.findNavController(binding.root).popBackStack()
                }
            }
            binding.deleteButton -> {
                processingDetailListViewModel.processDataDelete(context?:return)
                Navigation.findNavController(v).popBackStack()
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun materialModifyEvent(event: ProcessingMaterialModifyEvent) {
        CoroutineScope(Dispatchers.Default).launch {
            val data = processingDetailListViewModel.getDataById()
            CoroutineScope(Dispatchers.Main).launch {
                if(data != null && data.name == event.dataDetail.materialName && event.dataDetail.type == 2) {
                    StyleableToast.makeText(context?: return@launch, "현재 재료는 재료로 추가할 수 없습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                } else {
                    processingDetailListViewModel.dataModify(context?: return@launch, event.position, event.dataDetail)
                    binding.processingMDetailRecyclerView.adapter?.notifyItemChanged(event.position)
                }
            }
        }
    }
}