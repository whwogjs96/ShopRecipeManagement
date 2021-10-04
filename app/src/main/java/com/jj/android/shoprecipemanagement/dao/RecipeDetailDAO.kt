package com.jj.android.shoprecipemanagement.dao

import androidx.room.*
import com.jj.android.shoprecipemanagement.dto.ProcessingMDetailData
import com.jj.android.shoprecipemanagement.dto.RecipeDetailData

@Dao
interface RecipeDetailDAO {

    companion object {
        const val TABLE_NAME = "recipeDetailTable"
    }


    @Query("SELECT * FROM $TABLE_NAME WHERE processingMId=:id")
    fun findByParentId(id: Int) : List<RecipeDetailData>

    @Query("SELECT * FROM $TABLE_NAME WHERE id=:id")
    fun findById(id: Int) : RecipeDetailData?

    @Query("DELETE FROM $TABLE_NAME WHERE id=:parentId")
    fun deleteByParentId(parentId : Int)
    @Insert
    fun insert(item: RecipeDetailData)

    @Update
    fun update(item: RecipeDetailData)

    @Delete
    fun delete(item: RecipeDetailData)

}