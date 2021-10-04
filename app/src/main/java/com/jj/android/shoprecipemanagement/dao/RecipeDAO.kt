package com.jj.android.shoprecipemanagement.dao

import androidx.room.*
import com.jj.android.shoprecipemanagement.dto.RecipeData

/**
 * 레시피 저장
 */
@Dao
interface RecipeDAO {
    companion object {
        const val TABLE_NAME = "recipeTable"
    }

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): List<RecipeData>

    @Query("SELECT * FROM $TABLE_NAME WHERE recipeName=:name")
    fun findByName(name: String) : RecipeData?

    @Insert
    fun insert(item: RecipeData)

    @Update
    fun update(item: RecipeData)

    @Delete
    fun delete(item: RecipeData)
}