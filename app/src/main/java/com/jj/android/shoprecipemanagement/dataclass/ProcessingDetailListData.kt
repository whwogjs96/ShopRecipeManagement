package com.jj.android.shoprecipemanagement.dataclass

data class ProcessingDetailListData(
    var id : Int,
    var materialName: String,
    var usage : Int,
    var unitPrice : Double,
    var totalPrice : Double,
    var type : Int
)