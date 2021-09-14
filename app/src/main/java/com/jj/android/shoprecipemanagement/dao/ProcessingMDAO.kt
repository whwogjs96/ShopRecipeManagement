package com.jj.android.shoprecipemanagement.dao

import androidx.room.*
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData

/**
 * 여기가 합성재료의 이름을 저장하는 공간
 */
@Dao
interface ProcessingMDAO {
    companion object {
        const val TABLE_NAME = "processingMaterialTable"
    }

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<ProcessingMaterialData>

    @Query("SELECT * FROM $TABLE_NAME WHERE name=:name")
    fun findByName(name: String) : ProcessingMaterialData?

    @Query("SELECT * FROM $TABLE_NAME WHERE id=:id")
    fun findById(id: Int) : ProcessingMaterialData?

    @Insert
    fun insert(item: ProcessingMaterialData)

    @Update
    fun update(item: ProcessingMaterialData)

    @Delete
    fun delete(item: ProcessingMaterialData)
}