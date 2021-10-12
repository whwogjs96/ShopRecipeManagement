package com.jj.android.shoprecipemanagement.eventbus

import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.dto.RecipeDetailData

class DummyEvent()

class MaterialModifyEvent(var data : MaterialData, val position: Int)

class MaterialDeleteEvent(var data : MaterialData)

class ProcessingMaterialModifyEvent(var dataDetail : ProcessingDetailListData, val position: Int)

class DataInRecipeDeletedEvent(var recipeDetailData : RecipeDetailData) //레시피 상세 화면에서 재료 제거