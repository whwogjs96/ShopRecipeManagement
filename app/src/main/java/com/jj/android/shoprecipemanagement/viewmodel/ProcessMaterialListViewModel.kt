package com.jj.android.shoprecipemanagement.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.jj.android.shoprecipemanagement.dao.MaterialDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDetailDAO
import com.jj.android.shoprecipemanagement.database.MaterialDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMaterialDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingListData
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData
import com.jj.android.shoprecipemanagement.util.DatabaseCallUtil

//외부 혼합 재료 관련 데이터 처리
class ProcessMaterialListViewModel : ViewModel()  {

    lateinit var materialDao: MaterialDAO
    var processDataList = ArrayList<ProcessingListData>()
    var materialList = ArrayList<MaterialData>()
    var isDataUpdatable = false

    fun initDAO(context: Context) {
        val materialDB = MaterialDataBase.getInstance(context)!!
        materialDao = materialDB.materialDao()
    }

    fun getList() {
        clear()
        materialList.addAll(materialDao.getAll())
        processDataList.addAll(DatabaseCallUtil.getProcessMaterialList())
    }

    fun clear() {
        processDataList.clear()
        materialList.clear()
    }
}