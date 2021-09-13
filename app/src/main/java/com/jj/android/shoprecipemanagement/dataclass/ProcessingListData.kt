package com.jj.android.shoprecipemanagement.dataclass

data class ProcessingListData(
    var id : Int,
    var name : String,
    var unitPricePerGram : Double,
    var usage : Int,
    var price : Double
)