package com.jj.android.shoprecipemanagement.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.jj.android.shoprecipemanagement.dao.MaterialDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDetailDAO
import com.jj.android.shoprecipemanagement.database.MaterialDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMDetailDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMaterialDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingListData
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData
import com.jj.android.shoprecipemanagement.util.DatabaseCallUtil

//외부 합성 재료 관련 데이터 처리
class ProcessMaterialListViewModel : ViewModel()  {

    lateinit var processMaterialDao : ProcessingMDAO
    lateinit var processMDetailDao : ProcessingMDetailDAO
    lateinit var materialDao: MaterialDAO
    var dataList = ArrayList<ProcessingMaterialData>()
    var processDataList = ArrayList<ProcessingListData>()
    var materialList = ArrayList<MaterialData>()

    fun initDAO(context: Context) {
        val db = ProcessingMaterialDataBase.getInstance(context)!!
        processMaterialDao = db.processingMaterialDao()
        val detailDB = ProcessingMDetailDataBase.getInstance(context)!!
        processMDetailDao = detailDB.processingMDetailDao()
        val materialDB = MaterialDataBase.getInstance(context)!!
        materialDao = materialDB.materialDao()
    }

    fun getList() {
        clear()
        materialList.addAll(materialDao.getAll())
        dataList.addAll(processMaterialDao.getAll())
        dataList.forEach {
            getProcessingList(it)
        }
    }

    fun getProcessingList(item : ProcessingMaterialData) {
        val pData = DatabaseCallUtil.calculateProcessingData(item)
        processDataList.add(pData)
    }

    fun clear() {
        processDataList.clear()
        dataList.clear()
        materialList.clear()
    }
}