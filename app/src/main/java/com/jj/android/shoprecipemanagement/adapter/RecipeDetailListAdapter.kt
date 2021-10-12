package com.jj.android.shoprecipemanagement.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.R
import com.jj.android.shoprecipemanagement.database.ProcessingMaterialDataBase
import com.jj.android.shoprecipemanagement.dataclass.ProcessingDetailListData
import com.jj.android.shoprecipemanagement.dialog.ContentsAlertDialog
import com.jj.android.shoprecipemanagement.dto.ProcessingMDetailData
import com.jj.android.shoprecipemanagement.dto.RecipeDetailData
import com.jj.android.shoprecipemanagement.eventbus.DataInRecipeDeletedEvent
import com.jj.android.shoprecipemanagement.listener.ItemTouchListener
import com.jj.android.shoprecipemanagement.util.DatabaseCallUtil
import com.jj.android.shoprecipemanagement.util.RecipeDatabaseCallUtil
import com.jj.android.shoprecipemanagement.viewholder.MaterialInProcessingListViewHolder
import com.muddzdev.styleabletoast.StyleableToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

class RecipeDetailListAdapter(
        private val context: Context,
        var dataList: ArrayList<ProcessingDetailListData>
) : RecyclerView.Adapter<MaterialInProcessingListViewHolder>(), ItemTouchListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialInProcessingListViewHolder {
        return MaterialInProcessingListViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(
                                context
                        ), R.layout.item_processing_material_list, parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: MaterialInProcessingListViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onMove(oldPosition: Int, newPosition: Int) {
        val data = dataList[oldPosition]
        dataList.removeAt(oldPosition)
        dataList.add(newPosition, data)
        notifyItemMoved(oldPosition, newPosition)
    }

    override fun onSwipe(position: Int, direction: Int) {
        val data = dataList[position]
        val dialog = ContentsAlertDialog(context)
        dialog.doneEvent = {
            CoroutineScope(Dispatchers.Default).launch {
                if (data.id != 0) {
                    CoroutineScope(Dispatchers.Main).launch {
                        EventBus.getDefault().post(DataInRecipeDeletedEvent(RecipeDetailData(
                                id = data.id,
                                recipeId = 0,
                                materialInRecipeName = data.materialName,
                                type = data.type,
                                usage = data.usage,
                                index = position
                        )))
                        dataList.removeAt(position)
                        StyleableToast.makeText(context, "제거되었습니다.", Toast.LENGTH_SHORT, R.style.completeToastStyle).show()
                        notifyDataSetChanged()
                    }
                }
            }
        }
        dialog.setContent("${data.materialName}, 제거하시겠습니까?")
        dialog.show()
        notifyDataSetChanged()
    }
}