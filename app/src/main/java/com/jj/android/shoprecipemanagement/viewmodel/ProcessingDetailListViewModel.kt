package com.jj.android.shoprecipemanagement.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dao.MaterialDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDAO
import com.jj.android.shoprecipemanagement.dao.ProcessingMDetailDAO
import com.jj.android.shoprecipemanagement.database.MaterialDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMaterialDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dto.ProcessingMDetailData
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData
import com.jj.android.shoprecipemanagement.util.DatabaseCallUtil
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.*
import java.lang.NullPointerException

//혼합 재료 내부 리스트 처리
class ProcessingDetailListViewModel: ViewModel() {

    lateinit var processMaterialDao : ProcessingMDAO
    lateinit var processMDetailDao : ProcessingMDetailDAO
    lateinit var materialDao: MaterialDAO
    var dataList = ArrayList<ProcessingDetailListData>()
    var processingMaterialId = 0 //0일 경우 추가로 들어옴, 아닐 경우 수정

    fun initDAO(context: Context) {
        val db = ProcessingMaterialDataBase.getInstance(context)!!
        processMaterialDao = db.processingMaterialDao()
        processMDetailDao = db.processingMDetailDao()
        val materialDB = MaterialDataBase.getInstance(context)!!
        materialDao = materialDB.materialDao()
    }

    fun dataAdd(dataDetail: ProcessingDetailListData) {
        if(dataDetail.index == 0) dataDetail.index = dataList.size+1
        dataList.add(dataDetail)
    }

    fun getDataById() : ProcessingMaterialData? {
        return processMaterialDao.findById(processingMaterialId)
    }

    fun dataModify(context : Context, position : Int, item : ProcessingDetailListData) {
        dataList.find { it.materialName == item.materialName }.run {
            dataList[position] = item
            StyleableToast.makeText(context, "재료가 수정되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
        }
    }

    fun getDetailDataList(processingParentId: Int){
        DatabaseCallUtil.getProcessingDetailDataList(processingParentId).forEach { data ->
            dataList.find { it.id == data.id } ?: dataAdd(data)
        }
    }

    fun processingDataSave(context: Context, name: String, resultAction : () -> Unit) {
        if(name.isNotEmpty()) {
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    if(processingMaterialId == 0) { //추가인 경우
                        processMaterialDao.insert(ProcessingMaterialData(0,name))
                        val insertedData = processMaterialDao.findByName(name)!!
                        var index = 0
                        dataList.forEach {
                            index++
                            processMDetailDao.insert(ProcessingMDetailData(
                                id = 0,
                                processingMId = insertedData.id,
                                materialName = it.materialName,
                                type = it.type,
                                usage = it.usage,
                                index = index
                            ))
                        }
                    } else { //수정인 경우
                        try {
                            val modifyResult = processMaterialDao.findByName(name)// 같은 이름의 재료가 존재하는지 판단
                            if (modifyResult != null) {
                                if(modifyResult.id != processingMaterialId) {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        StyleableToast.makeText(context, "이름은 중복되면 안됩니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                                    }
                                    return@launch
                                }
                            } else {
                                processMaterialDao.update(ProcessingMaterialData(processingMaterialId, name))
                            }
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                            StyleableToast.makeText(context, "문제가 발생했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                            return@launch
                        }
                        var index = 0
                        dataList.forEach {
                            index++
                            val insertData = ProcessingMDetailData(
                                id = it.id,
                                processingMId = processingMaterialId,
                                materialName = it.materialName,
                                type = it.type,
                                usage = it.usage,
                                index = index
                            )
                            val data = processMDetailDao.findById(it.id)
                            if(data != null) {
                                processMDetailDao.update(insertData)
                            } else {
                                processMDetailDao.insert(insertData)
                            }
                        }
                    }
                    CoroutineScope(Dispatchers.Main).launch  {
                        resultAction()
                    }
                } catch (e: SQLiteConstraintException) {
                    e.printStackTrace()
                    CoroutineScope(Dispatchers.Main).launch {
                        StyleableToast.makeText(context, "재료 저장에 실패했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                    }
                }
            }
        } else {
            StyleableToast.makeText(context, "재료명을 입력해주세요.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
        }
    }

    fun processDataDelete(context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                processMaterialDao.delete(ProcessingMaterialData(processingMaterialId, ""))
                CoroutineScope(Dispatchers.Main).launch {
                    StyleableToast.makeText(context, "재료의 삭제가 완료되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    StyleableToast.makeText(context, "재료를 삭제하는데 실패했습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                }
            }
        }
    }
}