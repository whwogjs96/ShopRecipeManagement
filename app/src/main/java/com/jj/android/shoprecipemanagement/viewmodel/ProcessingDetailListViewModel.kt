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
import com.jj.android.shoprecipemanagement.database.ProcessingMDetailDataBase
import com.jj.android.shoprecipemanagement.database.ProcessingMaterialDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dto.ProcessingMDetailData
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NullPointerException

//합성 재료 내부 리스트 처리
class ProcessingDetailListViewModel: ViewModel() {

    lateinit var processMaterialDao : ProcessingMDAO
    lateinit var processMDetailDao : ProcessingMDetailDAO
    lateinit var materialDao: MaterialDAO
    var dataList = ArrayList<ProcessingDetailListData>()
    var processingMaterialId = 0 //0일 경우 추가로 들어옴, 아닐 경우 수정

    fun initDAO(context: Context) {
        val db = ProcessingMaterialDataBase.getInstance(context)!!
        processMaterialDao = db.processingMaterialDao()
        val detailDb = ProcessingMDetailDataBase.getInstance(context)!!
        processMDetailDao = detailDb.processingMDetailDao()
        val materialDB = MaterialDataBase.getInstance(context)!!
        materialDao = materialDB.materialDao()
    }

    fun dataAdd(dataDetail: ProcessingDetailListData) {
        dataList.add(dataDetail)
    }

    fun getDataById() : ProcessingMaterialData {
        return processMaterialDao.findById(processingMaterialId)
    }

    fun dataModify(context : Context, position : Int, item : ProcessingDetailListData) {
        dataList.find { it.materialName == item.materialName }.run {
            dataList.set(position, item)
            StyleableToast.makeText(context, "재료가 수정되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
        }
    }

    fun getDetailDataList(processingParentId: Int){
        val list = processMDetailDao.findByParentId(processingParentId)
        list.forEach { item ->
            if (item.type == 1) {
                val data = materialDao.findByName(item.materialName)
                if(data != null) {
                    dataAdd(
                        ProcessingDetailListData(
                            item.id,
                            item.materialName,
                            item.usage,
                            data.unitPricePerGram,
                            data.unitPricePerGram * item.usage,
                            item.type
                        )
                    )
                } else {
                    processMDetailDao.delete(item)
                }
            }
        }
        Log.e("???", list.toString())
    }

    fun processingDataSave(context: Context, name: String, resultAction : () -> Unit) {
        if(name.isNotEmpty()) {
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    if(processingMaterialId == 0) { //추가인 경우
                        processMaterialDao.insert(ProcessingMaterialData(0,name))
                        val insertedData = processMaterialDao.findByName(name)
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
                        CoroutineScope(Dispatchers.Main).launch  {
                            resultAction()
                        }
                    } else { //수정인 경우
                        val modifyResult = processMaterialDao.findByName(name)
                        try {
                            if(modifyResult.id != processingMaterialId) {
                                processMaterialDao.update(ProcessingMaterialData(processingMaterialId, name))
                            }
                        } catch (e: NullPointerException) {
                            processMaterialDao.update(ProcessingMaterialData(processingMaterialId, name))
                        }
                        var index = 0;
                        dataList.forEach {
                            index = 1
                            val data = processMDetailDao.findById(it.id)
                            Log.e("확인작업", data.toString())
                            if(data != null) {
                                processMDetailDao.update(ProcessingMDetailData(
                                    id = it.id,
                                    processingMId = processingMaterialId,
                                    materialName = it.materialName,
                                    type = it.type,
                                    usage = it.usage,
                                    index = index
                                ))
                            } else {
                                processMDetailDao.insert(ProcessingMDetailData(
                                    id = 0,
                                    processingMId = processingMaterialId,
                                    materialName = it.materialName,
                                    type = it.type,
                                    usage = it.usage,
                                    index = index
                                ))
                            }
                        }
                        Log.e("여기까지 확인", dataList.toString())
                        CoroutineScope(Dispatchers.Main).launch  {
                            resultAction()
                        }
                    }
                } catch (e: SQLiteConstraintException) {
                    CoroutineScope(Dispatchers.Main).launch {
                        StyleableToast.makeText(context, "이름은 중복되면 안됩니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                    }
                }
            }
        } else {
            StyleableToast.makeText(context, "재료명을 입력해주세요.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
        }
    }

    fun processDataDelete(context: Context) {
        CoroutineScope(Dispatchers.Default).launch{
            processMDetailDao.deleteByParentId(processingMaterialId)
            processMaterialDao.delete(ProcessingMaterialData(processingMaterialId, ""))
        }
        StyleableToast.makeText(context, "재료의 삭제가 완료되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
    }
}