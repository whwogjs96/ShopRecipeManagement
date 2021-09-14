package com.jj.android.shoprecipemanagement.listener

interface ItemTouchListener {
    fun onMove(oldPosition: Int, newPosition: Int)

    fun onSwipe(position: Int, direction: Int)
}