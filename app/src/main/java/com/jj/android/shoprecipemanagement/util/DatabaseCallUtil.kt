package com.jj.android.shoprecipemanagement.util

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
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
import com.jj.android.shoprecipemanagement.dataclass.RecipeListData
import com.jj.android.shoprecipemanagement.dto.*
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException

object DatabaseCallUtil {

    private var materialDao: MaterialDAO
    private var processMaterialDao: ProcessingMDAO
    private var processMDetailDao: ProcessingMDetailDAO
    private var recipeDao: RecipeDAO
    private var recipeDetailDao: RecipeDetailDAO

    init {
        //db 생성
        val db = ProcessingMaterialDataBase.getInstance(App.context())!!
        val materialDB = MaterialDataBase.getInstance(App.context())!!
        val recipeDb = RecipeDataBase.getInstance(App.context())!!

        //dao 추가
        processMaterialDao = db.processingMaterialDao()
        processMDetailDao = db.processingMDetailDao()
        materialDao = materialDB.materialDao()
        recipeDao = recipeDb.recipeDao()
        recipeDetailDao = recipeDb.recipeDetailDao()
    }

    fun getMaterialList(): List<MaterialData> {
        return materialDao.getAll()
    }

    fun getMaterialFindByName(name :String): MaterialData? {
        return materialDao.findByName(name)
    }

    fun getProcessMaterialFindByName(name : String): ProcessingMaterialData? {
        return processMaterialDao.findByName(name)
    }

    //혼합 재료 관련
    fun getProcessMaterialFromMaterial(materialName : String, processingDetailData : ProcessingMDetailData) : ProcessingDetailListData?{
        var result : ProcessingDetailListData? = null
        val data = materialDao.findByName(materialName)
        if(data != null) {
            processingDetailData.let {
                result = ProcessingDetailListData(
                        it.id,
                        it.materialName,
                        it.usage,
                        data.unitPricePerGram,
                        data.unitPricePerGram * it.usage,
                        it.type,
                        it.index
                )
            }
        } else {
            processMDetailDao.delete(processingDetailData)
        }
        return result
    }

    //혼합 재료 관련
    private fun getProcessMaterialFromProcessingMaterial(materialName : String, processingDetailData : ProcessingMDetailData) : ProcessingDetailListData?{
        var result : ProcessingDetailListData? = null
        val data = processMaterialDao.findByName(materialName)
        if (data != null) {
            val addedItem = calculateProcessingData(data)
            processingDetailData.let {
                result = ProcessingDetailListData(
                        it.id,
                        it.materialName,
                        it.usage,
                        addedItem.unitPricePerGram,
                        addedItem.unitPricePerGram * it.usage,
                        it.type,
                        it.index
                )
            }
            //일단 여기에 혼합재료를 더하면 됩니다.
        } else {
            processMDetailDao.delete(processingDetailData)
        }
        return result
    }


    fun getProcessingDetailDataList(processingParentId: Int): ArrayList<ProcessingDetailListData> {
        val list = processMDetailDao.findByParentId(processingParentId)
        val dataList = ArrayList<ProcessingDetailListData>()
        list.forEach { item ->
            if (item.type == 1) {
                getProcessMaterialFromMaterial(item.materialName, item)?.let {
                    dataList.add(it)
                }
            } else {
                getProcessMaterialFromProcessingMaterial(item.materialName, item)?.let {
                    dataList.add(it)
                }
            }
        }
        dataList.sortBy { it.index }
        return dataList
    }

    //재귀형 호출이기 때문에 db는 주입형태로 처리하자...
    fun calculateProcessingData(item: ProcessingMaterialData): ProcessingListData {
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

}