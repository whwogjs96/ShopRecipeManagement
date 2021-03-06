package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.FragmentMaterialListBinding
import com.jj.android.shoprecipemanagement.dialog.MaterialPrecessDialog
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.eventbus.MaterialDeleteEvent
import com.jj.android.shoprecipemanagement.eventbus.MaterialModifyEvent
import com.jj.android.shoprecipemanagement.result.MaterialDialogResult
import com.jj.android.shoprecipemanagement.viewmodel.MaterialListViewModel
import com.jj.android.shoprecipemanagement.viewmodel.ProcessMaterialListViewModel
import com.jj.android.shoprecipemanagement.viewmodel.RecipeListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MaterialListFragment: CommonFragment<FragmentMaterialListBinding>(R.layout.fragment_material_list), View.OnClickListener{

    private val materialListViewModel : MaterialListViewModel by activityViewModels()
    private val processingListViewModel : ProcessMaterialListViewModel by activityViewModels()
    private val recipeListViewModel : RecipeListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.materialViewModel = materialListViewModel
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val c = context?:return
        materialListViewModel.initDAO(c)
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Default).launch {
            materialListViewModel.getList()
            withContext(Dispatchers.Main) {
                binding.materialRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
        binding.materialAddButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.materialAddButton -> {
                val dialog = MaterialPrecessDialog(context?:return, 1)
                dialog.setResult(object : MaterialDialogResult {
                    override fun finish(data: MaterialData) {
                        materialListViewModel.dataAdd(context?:return, data)
                        binding.materialRecyclerView.adapter?.notifyDataSetChanged()
                    }
                })
                dialog.show()
            }
        }
    }

    /**
     * 다른 프래그먼트의 값이 변경되어야 하는 경우 이 코드를 사용
     * 이 코드에 다른 프래그먼트 변경을 알려주는 코드 작성
     */
    private fun fragmentUpdateNotify() {
        processingListViewModel.isDataUpdatable = true
        recipeListViewModel.isDataUpdatable = true
    }

    /**
     * 재료 내용 변경
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun materialModifyEvent(event: MaterialModifyEvent) {
        materialListViewModel.dataModify(context?:return, event.position, event.data)
        fragmentUpdateNotify()
        binding.materialRecyclerView.adapter?.notifyItemChanged(event.position)
    }

    /**
     * 재료 내용 삭제
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun materialDeleteEvent(event: MaterialDeleteEvent) {
        materialListViewModel.dataDelete(context?:return, event.data)
        fragmentUpdateNotify()
        binding.materialRecyclerView.adapter?.notifyDataSetChanged()
    }
}