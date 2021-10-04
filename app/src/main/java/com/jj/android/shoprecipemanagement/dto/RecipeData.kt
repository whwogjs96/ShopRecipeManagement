package com.jj.android.shoprecipemanagement.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        tableName = "recipeTable",
        indices = [Index(value = ["recipeName"], unique = true)]
)
data class RecipeData(
        @PrimaryKey(autoGenerate = true) var id: Int,
        @ColumnInfo(name = "recipeName")var recipeName : String
)
