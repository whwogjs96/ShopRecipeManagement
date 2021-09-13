package com.jj.android.shoprecipemanagement.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "processingMaterialTable",
    indices = [Index(value = ["name"], unique = true)]
)
data class ProcessingMaterialData(
    @PrimaryKey(autoGenerate = true) var id : Int,
    @ColumnInfo(name = "name") var name: String
)
