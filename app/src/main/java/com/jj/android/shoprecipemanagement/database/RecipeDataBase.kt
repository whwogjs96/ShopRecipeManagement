package com.jj.android.shoprecipemanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jj.android.shoprecipemanagement.dao.RecipeDAO
import com.jj.android.shoprecipemanagement.dao.RecipeDetailDAO
import com.jj.android.shoprecipemanagement.dto.RecipeData
import com.jj.android.shoprecipemanagement.dto.RecipeDetailData

@Database(
        entities= [RecipeData::class, RecipeDetailData::class],
        version = 1,
        exportSchema = false
)
abstract class  RecipeDataBase : RoomDatabase() {
    abstract fun recipeDao() : RecipeDAO
    abstract fun recipeDetailDao() : RecipeDetailDAO

    companion object {
        private var INSTANCE : RecipeDataBase? = null
        fun getInstance(context : Context) : RecipeDataBase? {
            if(INSTANCE == null) {
                synchronized(RecipeDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            RecipeDataBase::class.java, "recipeTable"
                    ).fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }
    }

    fun destroyInstance() {
        INSTANCE = null
    }
}