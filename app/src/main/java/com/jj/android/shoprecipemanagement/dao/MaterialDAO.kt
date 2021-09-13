package com.jj.android.shoprecipemanagement.dao

import androidx.room.*
import com.jj.android.shoprecipemanagement.dto.MaterialData

@Dao
interface MaterialDAO {

    companion object {
        const val TABLE_NAME = "materialTable"
    }
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<MaterialData>

    @Query("SELECT * FROM $TABLE_NAME WHERE name=:name")
    fun findByName(name: String) : MaterialData?

    @Insert
    fun insert(item: MaterialData)

    @Update
    fun update(item: MaterialData)

    @Delete
    fun delete(item: MaterialData)
}