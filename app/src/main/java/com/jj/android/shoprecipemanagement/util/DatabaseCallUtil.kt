package com.jj.android.shoprecipemanagement.util

import android.content.Context
import android.util.Log
import com.jj.android.shoprecipemanagement.application.App
import com.jj.android.shoprecipemanagement.dao.MaterialDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDetailDAO
import com.jj.android.shoprecipemanagement.database.MaterialDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMaterialDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingListData
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData

object DatabaseCallUtil {

    private var materialDao: MaterialDAO
    private var processMaterialDao: ProcessingMDAO
    private var processMDetailDao: ProcessingMDetailDAO

    init {
        val db = ProcessingMaterialDataBase.getInstance(App.context())!!
        processMaterialDao = db.processingMaterialDao()
        processMDetailDao = db.processingMDetailDao()
        val materialDB = MaterialDataBase.getInstance(App.context())!!
        materialDao = materialDB.materialDao()
    }

    //재귀형 호출이기 때문에 db는 주입형태로 처리하자...
    fun calculateProcessingData(
        item: ProcessingMaterialData
    ): ProcessingListData {
        //나중에 반환할 타입
        val resultData = ProcessingListData(item.id, item.name, 0.0,0,0.0)
        processMDetailDao.findByParentId(item.id).forEach { detailData ->
            if(detailData.type == 1) {
                resultData.usage += detailData.usage
                val data = materialDao.findByName(detailData.materialName)
                if(data != null) {
                    resultData.price += data.unitPricePerGram*detailData.usage
                } else {
                    processMDetailDao.delete(detailData)
                }
            } else {
                val data = processMaterialDao.findByName(detailData.materialName)
                if(data != null) {
                    val processData = calculateProcessingData(data)
                    resultData.usage += detailData.usage
                    resultData.price += processData.unitPricePerGram*detailData.usage
                } else {
                    processMDetailDao.delete(detailData)
                }
            }
        }
        if(resultData.usage != 0) resultData.apply {
            unitPricePerGram = price /usage.toDouble()
        }
        return resultData
    }

    fun getProcessMaterialList() : ArrayList<ProcessingListData>{
        val list = ArrayList<ProcessingListData>()
        processMaterialDao.getAll().forEach {
            list.add(calculateProcessingData(it))
        }
        return list
    }
}