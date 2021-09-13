package com.jj.android.shoprecipemanagement.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.dao.MaterialDAO
import com.jj.android.shoprecipemanagement.database.MaterialDataBase
import com.jj.android.shoprecipemanagement.dto.MaterialData
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.*

class MaterialListViewModel : ViewModel() {
    lateinit var materialDao: MaterialDAO
    var dataList : ArrayList<MaterialData> = ArrayList()

    fun initDAO(context: Context) {
        val db = MaterialDataBase.getInstance(context)!!
        materialDao = db.materialDao()
    }

    suspend fun getList() = withContext(Dispatchers.Default) {
        dataList.clear()
        dataList.addAll(materialDao.getAll())
    }

    fun dataAdd(context: Context, item: MaterialData) {
        for (data in dataList) {
            if(data.name == item.name) {
                StyleableToast.makeText(context, "재료명은 중복될 수 없습니다.", Toast.LENGTH_SHORT, R.style.errorToastStyle).show()
                return
            }
        }
        StyleableToast.makeText(context, "재료가 추가되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
        dataList.add(item)
        CoroutineScope(Dispatchers.Default).launch {
            materialDao.insert(item)
        }

    }

    fun dataModify(context: Context, position : Int, item: MaterialData) {
        dataList.find { it.name == item.name }.run {
            dataList.set(position, item)
            CoroutineScope(Dispatchers.Default).launch {
                materialDao.update(item)
            }
            StyleableToast.makeText(context, "재료가 수정되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
        }
    }

    fun dataDelete(context: Context, data :MaterialData) {
        for(position in 0 until dataList.size) {
            if(dataList[position].name == data.name) {
                dataList.removeAt(position)
                CoroutineScope(Dispatchers.Default).launch {
                    materialDao.delete(data)
                }
                StyleableToast.makeText(context, "${data.name}(이)가 제거되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
                break
            }
        }
    }
}