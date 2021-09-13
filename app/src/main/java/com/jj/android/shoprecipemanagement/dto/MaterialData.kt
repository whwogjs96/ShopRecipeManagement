package com.jj.android.shoprecipemanagement.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materialTable")
data class MaterialData(
    @PrimaryKey var name : String,
    @ColumnInfo(name = "unitPrice") var unitPrice: Int,
    @ColumnInfo(name = "weight") var weight: Int,
    @ColumnInfo(name = "unitPricePerGram") var unitPricePerGram: Double
)