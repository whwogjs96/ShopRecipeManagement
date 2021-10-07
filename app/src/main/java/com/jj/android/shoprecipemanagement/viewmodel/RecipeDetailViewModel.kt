package com.jj.android.shoprecipemanagement.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dto.RecipeData
import com.jj.android.shoprecipemanagement.util.DatabaseCallUtil
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecipeDetailViewModel : ViewModel() {
    val dataList = ArrayList<ProcessingDetailListData>()
    var recipeId = 0

    fun getDataById(): RecipeData? {
        return DatabaseCallUtil.getRecipeById(recipeId)
    }

    fun dataAdd(dataDetail: ProcessingDetailListData) {
        dataDetail.index = dataList.size+1
        dataList.add(dataDetail)
    }

    fun dataModify(context : Context, position : Int, item : ProcessingDetailListData) {
        dataList[position] = item
        StyleableToast.makeText(context, "재료가 수정되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
    }

    fun recipeDataSave(context: Context, name: String, resultAction : () -> Unit) {
        if(name.isNotEmpty()) {
            if(recipeId == 0 ) { //추가
                CoroutineScope(Dispatchers.Default).launch {
                    DatabaseCallUtil.recipeAdd(context, name, dataList)
                }
            } else { //수정

            }
       }
    }
}