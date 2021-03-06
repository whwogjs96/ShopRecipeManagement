package com.jj.android.shoprecipemanagement.dto

import androidx.room.*

@Entity(
        tableName = "recipeDetailTable",
        indices = [Index(value = arrayOf("recipeId"))],
        foreignKeys = [
            ForeignKey(
                    entity = RecipeData::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("recipeId"),
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class RecipeDetailData(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "recipeId") var recipeId: Int,
        @ColumnInfo(name = "materialInRecipeName") var materialInRecipeName : String,
        @ColumnInfo(name = "type") var type : Int,
        @ColumnInfo(name = "usage") var usage : Int,
        @ColumnInfo(name = "index") var index : Int
)