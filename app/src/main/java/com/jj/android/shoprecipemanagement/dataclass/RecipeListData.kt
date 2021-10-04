package com.jj.android.shoprecipemanagement.dataclass

data class RecipeListData(
        var id : Int,
        var name : String,
        var unitPricePerGram : Double,
        var usage : Int,
        var price : Double,
        var materialCount : Int
)
