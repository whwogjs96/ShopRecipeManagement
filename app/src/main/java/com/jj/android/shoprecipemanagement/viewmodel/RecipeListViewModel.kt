package com.jj.android.shoprecipemanagement.viewmodel

import androidx.lifecycle.ViewModel
import com.jj.android.shoprecipemanagement.dataclass.RecipeListData
import com.jj.android.shoprecipemanagement.util.RecipeDatabaseCallUtil

class RecipeListViewModel: ViewModel() {
    var recipeDataList = ArrayList<RecipeListData>()
    var isDataUpdatable = false

    fun getList() {
        recipeDataList.addAll(RecipeDatabaseCallUtil.getRecipeList())
    }

    fun clear() {
        recipeDataList.clear()
    }
}
