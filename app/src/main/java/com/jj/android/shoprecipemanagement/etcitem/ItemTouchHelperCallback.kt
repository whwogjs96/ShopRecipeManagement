package com.jj.android.shoprecipemanagement.etcitem

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.jj.android.shoprecipemanagement.listener.ItemTouchListener

class ItemTouchHelperCallback(val listener: ItemTouchListener) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, dragFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        listener.onMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwipe(viewHolder.adapterPosition, direction)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }
}