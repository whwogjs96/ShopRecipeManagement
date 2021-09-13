package com.jj.android.shoprecipemanagement.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.jj.android.shoprecipemanagement.dao.MaterialDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDetailDAO
import com.jj.android.shoprecipemanagement.database.MaterialDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMDetailDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMaterialDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingListData
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.dto.ProcessingMDetailData
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        val data = processMDetailDao.findByParentId(item.id)
        val pData = ProcessingListData(item.id, item.name, 0.0,0,0.0)
        data.forEach { processingData ->
            pData.usage+=processingData.usage
            materialList.find { it.name == processingData.materialName }
                ?.apply {
                    pData.unitPricePerGram += this.unitPricePerGram
                    pData.price+= this.unitPricePerGram*processingData.usage
                }
        }
        processDataList.add(pData)
    }

    fun clear() {
        processDataList.clear()
        dataList.clear()
        materialList.clear()
    }
}