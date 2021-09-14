package com.jj.android.shoprecipemanagement.dataclass

data class ProcessingDetailListData(
    var id : Int,
    var materialName: String,
    var usage : Int,
    var unitPricePerGram : Double,
    var totalPrice : Double,
    var type : Int,
    var index : Int
)