package com.jj.android.shoprecipemanagement.util

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.widget.Toast
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.application.App
import com.jj.android.shoprecipemanagement.dao.RecipeDAO
import com.jj.android.shoprecipemanagement.dao.RecipeDetailDAO
import com.jj.android.shoprecipemanagement.database.RecipeDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dataclass.RecipeListData
import com.jj.android.shoprecipemanagement.dto.RecipeData
import com.jj.android.shoprecipemanagement.dto.RecipeDetailData
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

object RecipeDatabaseCallUtil {
    private var recipeDao: RecipeDAO
    private var recipeDetailDao: RecipeDetailDAO

    init {
        val recipeDb = RecipeDataBase.getInstance(App.context())!!
        recipeDao = recipeDb.recipeDao()
        recipeDetailDao = recipeDb.recipeDetailDao()
    }


    //레시피 재료 관련
    private fun getProcessMaterialFromMaterial(materialName : String, recipeDetailData : RecipeDetailData) : ProcessingDetailListData? {
        val data = DatabaseCallUtil.getMaterialFindByName(materialName)
        var result : ProcessingDetailListData? = null
        if (data != null) {
            recipeDetailData.let {
                result = ProcessingDetailListData(
                        it.id,
                        it.materialInRecipeName,
                        it.usage,
                        data.unitPricePerGram,
                        data.unitPricePerGram * it.usage,
                        it.type,
                        it.index
                )
            }

        } else {
            recipeDetailDao.delete(recipeDetailData)
        }
        return result
    }


    //레시피 재료 관련
    private fun getProcessMaterialFromProcessingMaterial(materialName : String, recipeDetailData : RecipeDetailData) : ProcessingDetailListData?{
        var result : ProcessingDetailListData? = null
        val data = DatabaseCallUtil.getProcessMaterialFindByName(materialName)
        if (data != null) {
            val addedItem = DatabaseCallUtil.calculateProcessingData(data)
            recipeDetailData.let {
                result = ProcessingDetailListData(
                        it.id,
                        it.materialInRecipeName,
                        it.usage,
                        addedItem.unitPricePerGram,
                        addedItem.unitPricePerGram * it.usage,
                        it.type,
                        it.index
                )
            }
            //일단 여기에 혼합재료를 더하면 됩니다.
        } else {
            recipeDetailDao.delete(recipeDetailData)
        }
        return result
    }

    fun getRecipeList() : ArrayList<RecipeListData> {
        val list = ArrayList<RecipeListData>()
        recipeDao.getAll().forEach {
            list.add(calculateRecipeData(it))
        }
        return list
    }

    fun getRecipeById(recipeId: Int): RecipeData? {
        return recipeDao.findById(recipeId)
    }

    fun getRecipeByName(name: String): RecipeData? {
        return recipeDao.findByName(name)
    }

    fun recipeAdd(context: Context, name: String, dataList: ArrayList<ProcessingDetailListData>, resultAction : () -> Unit) {
        try {
            if(getRecipeByName(name) != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    StyleableToast.makeText(context, "레시피명은 중복될 수 없습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                }
            }
            recipeDao.insert(RecipeData(0, name))
            val insertData = getRecipeByName(name)!!
            var index = 0
            dataList.forEach {
                index++
                recipeDetailDao.insert(
                        RecipeDetailData(
                                id = 0,
                                recipeId = insertData.id,
                                materialInRecipeName = it.materialName,
                                type = it.type,
                                usage = it.usage,
                                index = index
                        )
                )//여기에 레시피 내부 재료 상세 추가 기능 구현
            }
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "레시피 저장에 성공했습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
                resultAction()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "레시피 저장에 실패했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
            }
        }
    }

    fun recipeModify(context: Context, name: String, recipeId : Int, dataList: ArrayList<ProcessingDetailListData>, resultAction : () -> Unit) {
        if(name.isNotEmpty()) {
            try {
                val duplicationData = recipeDao.findByName(name)
                if (duplicationData != null) {
                    if(duplicationData.id != recipeId) {
                        CoroutineScope(Dispatchers.Main).launch {
                            StyleableToast.makeText(context, "이름은 중복되면 안됩니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                        }
                        return
                    }
                } else {
                    recipeDao.update(RecipeData(recipeId, name))
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
                StyleableToast.makeText(context, "문제가 발생했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                return
            }
            try {
                var index = 0
                dataList.forEach {
                    index++
                    val insertData = RecipeDetailData(
                            id = it.id,
                            recipeId = recipeId,
                            materialInRecipeName = it.materialName,
                            type = it.type,
                            usage = it.usage,
                            index = index
                    )
                    val data = recipeDetailDao.findById(it.id)
                    if(data != null) {
                        recipeDetailDao.update(insertData)
                    } else {
                        recipeDetailDao.insert(insertData)
                    }
                }
                CoroutineScope(Dispatchers.Main).launch  {
                    StyleableToast.makeText(context, "레시피를 수정했습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
                    resultAction()
                }
            } catch (s : SQLiteConstraintException) {
                s.printStackTrace()
                CoroutineScope(Dispatchers.Main).launch {
                    StyleableToast.makeText(context, "레시피 저장에 실패했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                }
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "레시피명을 입력해주세요.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
            }
        }
    }

    fun getRecipeDetailDataList(recipeId: Int) : ArrayList<ProcessingDetailListData> {
        val list = recipeDetailDao.findByParentId(recipeId)
        val dataList = ArrayList<ProcessingDetailListData>()
        Log.e("확인용", list.toString())
        list.forEach { item ->
            if(item.type == 1) {
                getProcessMaterialFromMaterial(item.materialInRecipeName, item)?.let {
                    dataList.add(it)
                }
            } else {
                getProcessMaterialFromProcessingMaterial(item.materialInRecipeName, item)?.let {
                    dataList.add(it)
                }
            }
        }
        dataList.sortBy { it.index }
        return dataList
    }


    private fun calculateRecipeData(item: RecipeData) : RecipeListData {
        val resultData = RecipeListData(item.id, item.recipeName, 0.0,0,0.0, 0)
        recipeDetailDao.findByParentId(item.id).forEach { detailData ->
            if (detailData.type == 1) {
                resultData.usage += detailData.usage
                val data = DatabaseCallUtil.getMaterialFindByName(detailData.materialInRecipeName)
                if (data != null) {
                    resultData.price += data.unitPricePerGram * detailData.usage
                    resultData.materialCount++
                } else {
                    recipeDetailDao.delete(detailData)
                }
            } else {
                val data = DatabaseCallUtil.getProcessMaterialFindByName(detailData.materialInRecipeName)
                if (data != null) {
                    val processData = DatabaseCallUtil.calculateProcessingData(data)
                    resultData.usage += detailData.usage
                    resultData.price += processData.unitPricePerGram * detailData.usage
                    resultData.materialCount++
                } else {
                    recipeDetailDao.delete(detailData)
                }
            }
        }
        if (resultData.usage != 0) resultData.apply {
            unitPricePerGram = price / usage.toDouble()
        }
        return resultData
    }

    fun deleteRecipe(recipeId : Int, name : String) : Boolean {
        return try {
            recipeDao.delete(RecipeData(recipeId, name))
            true
        } catch (e : Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteRecipeDetailData(deletedData : RecipeDetailData): Boolean {
        return try {
            recipeDetailDao.delete(deletedData)
            true
        } catch (e : Exception) {
            false
        }
    }

    fun deleteRecipeDetailDataList(dataList : ArrayList<RecipeDetailData>) {
        dataList.forEach {
            deleteRecipeDetailData(it)
        }
    }
}