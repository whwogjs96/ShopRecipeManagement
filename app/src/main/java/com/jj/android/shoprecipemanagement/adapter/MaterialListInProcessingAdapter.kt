package com.jj.android.shoprecipemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.database.ProcessingMDetailDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dialog.ContentsAlertDialog
import com.jj.android.shoprecipemanagement.dto.ProcessingMDetailData
import com.jj.android.shoprecipemanagement.listener.ItemTouchListener
import com.jj.android.shoprecipemanagement.viewholder.MaterialInProcessingListViewHolder
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MaterialListInProcessingAdapter(
    private val context: Context,
    var dataDetailList: ArrayList<ProcessingDetailListData>
) : RecyclerView.Adapter<MaterialInProcessingListViewHolder>(), ItemTouchListener {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MaterialInProcessingListViewHolder {
        return MaterialInProcessingListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(
                    context
                ), R.layout.item_processing_material_list, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MaterialInProcessingListViewHolder, position: Int) {
        holder.bind(dataDetailList[position])
    }

    override fun getItemCount(): Int {
        return dataDetailList.size
    }

    override fun onMove(oldPosition: Int, newPosition: Int) {
        val data = dataDetailList[oldPosition]
        dataDetailList.removeAt(oldPosition)
        dataDetailList.add(newPosition, data)
        notifyItemMoved(oldPosition, newPosition)
    }

    override fun onSwipe(position: Int, direction: Int) {
        val data = dataDetailList[position]
        val dialog = ContentsAlertDialog(context)
        dialog.doneEvent = {
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    if (data.id != 0) {
                        val detailDb = ProcessingMDetailDataBase.getInstance(context)!!
                        val processMDetailDao = detailDb.processingMDetailDao()
                        processMDetailDao.delete(
                            ProcessingMDetailData(
                                id = data.id,
                                processingMId = 0,
                                materialName = data.materialName,
                                type = data.type,
                                usage = data.usage,
                                index = position
                            )
                        )
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        dataDetailList.removeAt(position)
                        StyleableToast.makeText(context, "제거되었습니다.", Toast.LENGTH_SHORT).show()
                        notifyItemRemoved(position)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    CoroutineScope(Dispatchers.Main).launch {
                        StyleableToast.makeText(context, "제거에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        dialog.setContent("${data.materialName}, 제거하시겠습니까?")
        dialog.show()
        notifyDataSetChanged()
    }
}