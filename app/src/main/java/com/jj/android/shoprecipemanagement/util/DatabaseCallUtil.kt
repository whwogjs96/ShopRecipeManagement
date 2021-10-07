package com.jj.android.shoprecipemanagement.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.application.App
import com.jj.android.shoprecipemanagement.dao.*
import com.jj.android.shoprecipemanagement.database.MaterialDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMaterialDataBase
import com.jj.android.shoprecipemanagement.database.RecipeDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dataclass.ProcessingListData
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData
import com.jj.android.shoprecipemanagement.dto.RecipeData
import com.jj.android.shoprecipemanagement.dto.RecipeDetailData
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

object DatabaseCallUtil {

    private var materialDao: MaterialDAO
    private var processMaterialDao: ProcessingMDAO
    private var processMDetailDao: ProcessingMDetailDAO
    private var recipeDao: RecipeDAO
    private var recipeDetailDao: RecipeDetailDAO

    init {
        val db = ProcessingMaterialDataBase.getInstance(App.context())!!
        processMaterialDao = db.processingMaterialDao()
        processMDetailDao = db.processingMDetailDao()
        val materialDB = MaterialDataBase.getInstance(App.context())!!
        materialDao = materialDB.materialDao()
        val recipeDb = RecipeDataBase.getInstance(App.context())!!
        recipeDao = recipeDb.recipeDao()
        recipeDetailDao = recipeDb.recipeDetailDao()
    }

    fun getMaterialList(): List<MaterialData> {
        return materialDao.getAll()
    }

    fun getProcessingDetailDataList(processingParentId: Int): ArrayList<ProcessingDetailListData> {
        val list = processMDetailDao.findByParentId(processingParentId)
        val dataList = ArrayList<ProcessingDetailListData>()
        list.forEach { item ->
            if (item.type == 1) {
                val data = materialDao.findByName(item.materialName)
                if (data != null) {
                    dataList.add(
                        ProcessingDetailListData(
                            item.id,
                            item.materialName,
                            item.usage,
                            data.unitPricePerGram,
                            data.unitPricePerGram * item.usage,
                            item.type,
                            item.index
                        )
                    )
                } else {
                    processMDetailDao.delete(item)
                }
            } else {
                val data = processMaterialDao.findByName(item.materialName)
                if (data != null) {
                    val addedItem = DatabaseCallUtil.calculateProcessingData(data)
                    dataList.add(
                        ProcessingDetailListData(
                            addedItem.id,
                            addedItem.name,
                            item.usage,
                            addedItem.unitPricePerGram,
                            addedItem.unitPricePerGram * item.usage,
                            item.type,
                            item.index
                        )
                    )
                    //일단 여기에 혼합재료를 더하면 됩니다.
                } else {
                    processMDetailDao.delete(item)
                }
            }
        }
        dataList.sortBy { it.index }
        return dataList
    }

    //재귀형 호출이기 때문에 db는 주입형태로 처리하자...
    fun calculateProcessingData(
        item: ProcessingMaterialData
    ): ProcessingListData {
        //나중에 반환할 타입
        val resultData = ProcessingListData(item.id, item.name, 0.0, 0, 0.0)
        processMDetailDao.findByParentId(item.id).forEach { detailData ->
            if (detailData.type == 1) {
                resultData.usage += detailData.usage
                val data = materialDao.findByName(detailData.materialName)
                if (data != null) {
                    resultData.price += data.unitPricePerGram * detailData.usage
                } else {
                    processMDetailDao.delete(detailData)
                }
            } else {
                val data = processMaterialDao.findByName(detailData.materialName)
                if (data != null) {
                    val processData = calculateProcessingData(data)
                    resultData.usage += detailData.usage
                    resultData.price += processData.unitPricePerGram * detailData.usage
                } else {
                    processMDetailDao.delete(detailData)
                }
            }
        }
        if (resultData.usage != 0) resultData.apply {
            unitPricePerGram = price / usage.toDouble()
        }
        return resultData
    }

    fun getProcessMaterialList(): ArrayList<ProcessingListData> {
        val list = ArrayList<ProcessingListData>()
        processMaterialDao.getAll().forEach {
            list.add(calculateProcessingData(it))
        }
        return list
    }


    fun getRecipeById(recipeId: Int): RecipeData? {
        return recipeDao.findById(recipeId)
    }

    fun getRecipeByName(name: String): RecipeData? {
        return recipeDao.findByName(name)
    }

    fun recipeAdd(context: Context, name: String, dataList: ArrayList<ProcessingDetailListData>) {
        try {
            if(getRecipeByName(name) != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    StyleableToast.makeText(context, "재료명은 중복될 수 없습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
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
                        processingMId = insertData.id,
                        materialInRecipeName = it.materialName,
                        type = it.type,
                        usage = it.usage,
                        index = index
                    )
                )//여기에 레시피 내부 재료 상세 추가 기능 구현
            }
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "레시피 저장에 성공했습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CoroutineScope(Dispatchers.Main).launch {
                StyleableToast.makeText(context, "레시피 저장에 실패했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
            }
        }
    }
}