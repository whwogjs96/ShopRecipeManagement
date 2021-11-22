package com.jj.android.shoprecipemanagement.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.databinding.FragmentRecipeDetailBinding
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dialog.MaterialSelectDialog
import com.jj.android.shoprecipemanagement.eventbus.DataInRecipeDeletedEvent
import com.jj.android.shoprecipemanagement.eventbus.MaterialDeleteEvent
import com.jj.android.shoprecipemanagement.eventbus.ProcessingMaterialModifyEvent
import com.jj.android.shoprecipemanagement.result.ProcessingMaterialDialogResult
import com.jj.android.shoprecipemanagement.viewmodel.RecipeDetailViewModel
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RecipeDetailFragment : CommonFragment<FragmentRecipeDetailBinding>(R.layout.fragment_recipe_detail), View.OnClickListener {

    private val recipeDetailListViewModel : RecipeDetailViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeDetailListViewModel.recipeId = arguments?.getInt("id", 0) ?:0
        if(recipeDetailListViewModel.recipeId != 0) { //수정 모드
            CoroutineScope(Dispatchers.Default).launch {
                val data = recipeDetailListViewModel.getDataById()
                recipeDetailListViewModel.getDetailDataList()
                CoroutineScope(Dispatchers.Main).launch {
                    binding.recipeDetailRecyclerView.adapter?.notifyDataSetChanged()
                    binding.recipeNameView.setText(data?.recipeName?:"")
                }
            }
            binding.addButton.text = getString(R.string.modify)
            binding.recipeNameLayout.hint = getString(R.string.modifyRecipeName)
            binding.deleteButton.visibility = View.VISIBLE
        } else { //추가 모드
            binding.addButton.text = getString(R.string.add)
            binding.recipeNameLayout.hint = getString(R.string.addRecipeName)
            binding.deleteButton.visibility = View.GONE
        }
        binding.cancelButton.setOnClickListener(this)
        binding.materialAddButton.setOnClickListener(this)
        binding.addButton.setOnClickListener(this)
        binding.deleteButton.setOnClickListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.recipeDetailViewModel = recipeDetailListViewModel
        return binding.root
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.cancelButton -> {
                Navigation.findNavController(v).popBackStack()
            }
            binding.materialAddButton -> {
                val dialog = MaterialSelectDialog(binding.root.context, 1)
                dialog.setResult(object : ProcessingMaterialDialogResult {
                    override fun finish(dataDetail: ProcessingDetailListData) {
                        CoroutineScope(Dispatchers.Main).launch {
                            recipeDetailListViewModel.dataAdd(dataDetail)
                            StyleableToast.makeText(context?: return@launch, "재료가 추가되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
                            binding.recipeDetailRecyclerView.adapter?.notifyDataSetChanged()
                        }
                    }
                })
                dialog.show()
            }
            binding.addButton -> {
                val recipeName = binding.recipeNameView.text.toString()
                if(recipeName.isNotEmpty()) {
                    recipeDetailListViewModel.recipeDataSave(context?:return, recipeName) {
                        Navigation.findNavController(binding.root).popBackStack()
                    }
                } else {
                    StyleableToast.makeText(context?:return, "레시피명을 입력해주세요.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                }
            }
            binding.deleteButton -> {
                val c = context?:return
                CoroutineScope(Dispatchers.Default).launch {
                    if(recipeDetailListViewModel.recipeDelete(c)) {
                        withContext(Dispatchers.Main) {
                            Navigation.findNavController(binding.root).popBackStack()
                        }
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun materialModifyEvent(event: ProcessingMaterialModifyEvent) {
        CoroutineScope(Dispatchers.Main).launch {
            recipeDetailListViewModel.dataModify(context?: return@launch, event.position, event.dataDetail)
            binding.recipeDetailRecyclerView.adapter?.notifyItemChanged(event.position)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun materialDeleteEvent(event : DataInRecipeDeletedEvent) {
        recipeDetailListViewModel.deletedDataList.add(event.recipeDetailData)
    }
}