package com.jj.android.shoprecipemanagement.dto

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "processingMDetailTable",
    indices = [Index(value = arrayOf("processingMId"))],
    foreignKeys = [
        ForeignKey(
            entity = ProcessingMaterialData::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("processingMId"),
            onDelete = CASCADE
        )
    ]
)
data class ProcessingMDetailData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "processingMId")var processingMId: Int,
    @ColumnInfo(name = "materialName") var materialName : String,
    @ColumnInfo(name = "type") var type : Int,
    @ColumnInfo(name = "usage") var usage : Int,
    @ColumnInfo(name = "index") var index : Int
)