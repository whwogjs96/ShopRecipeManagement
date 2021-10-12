package com.jj.android.shoprecipemanagement.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dto.RecipeData
import com.jj.android.shoprecipemanagement.dto.RecipeDetailData
import com.jj.android.shoprecipemanagement.util.DatabaseCallUtil
import com.jj.android.shoprecipemanagement.util.RecipeDatabaseCallUtil
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class RecipeDetailViewModel : ViewModel() {
    val dataList = ArrayList<ProcessingDetailListData>()
    val deletedDataList = ArrayList<RecipeDetailData>()
    var recipeId = 0

    fun getDataById(): RecipeData? {
        return RecipeDatabaseCallUtil.getRecipeById(recipeId)
    }

    fun dataAdd(dataDetail: ProcessingDetailListData) {
        dataList.add(dataDetail)
    }

    fun dataModify(context : Context, position : Int, item : ProcessingDetailListData) {
        dataList[position] = item
        StyleableToast.makeText(context, "재료가 수정되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
    }

    fun getDetailDataList(){
        RecipeDatabaseCallUtil.getRecipeDetailDataList(recipeId).forEach { data ->
            dataAdd(data)
        }
    }

    fun recipeDataSave(context: Context, name: String, resultAction : () -> Unit) {
        if(name.isNotEmpty()) {
            if(recipeId == 0 ) { //추가
                CoroutineScope(Dispatchers.Default).launch {
                    RecipeDatabaseCallUtil.recipeAdd(context, name, dataList, resultAction)
                }
            } else { //수정
                CoroutineScope(Dispatchers.Default).launch {
                    RecipeDatabaseCallUtil.apply {
                        recipeModify(context, name, recipeId, dataList, resultAction)
                        deleteRecipeDetailDataList(deletedDataList)
                    }
                }
            }
       }
    }

    fun recipeDelete(context: Context) :Boolean {
        return if(RecipeDatabaseCallUtil.deleteRecipe(recipeId, "")) {
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "재료가 삭제되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
            }
            true
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "재료가 삭제되지 않았습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
            }
            false
        }
    }
}