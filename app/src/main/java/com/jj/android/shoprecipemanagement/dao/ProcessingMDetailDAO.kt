package com.jj.android.shoprecipemanagement.dao

import androidx.room.*
import com.jj.android.shoprecipemanagement.dto.ProcessingMDetailData

@Dao
interface ProcessingMDetailDAO {
    companion object {
        const val TABLE_NAME = "processingMDetailTable"
    }


    @Query("SELECT * FROM $TABLE_NAME WHERE processingMId=:id")
    fun findByParentId(id: Int) : List<ProcessingMDetailData>

    @Query("SELECT * FROM $TABLE_NAME WHERE id=:id")
    fun findById(id: Int) : ProcessingMDetailData?

    @Query("DELETE FROM $TABLE_NAME WHERE id=:parentId")
    fun deleteByParentId(parentId : Int)
    @Insert
    fun insert(item: ProcessingMDetailData)

    @Update
    fun update(item: ProcessingMDetailData)

    @Delete
    fun delete(item: ProcessingMDetailData)

}