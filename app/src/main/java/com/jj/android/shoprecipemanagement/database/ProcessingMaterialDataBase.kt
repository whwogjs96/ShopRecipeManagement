package com.jj.android.shoprecipemanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jj.android.shoprecipemanagement.dao.ProcessingMDAO
import com.jj.android.shoprecipemanagement.dto.ProcessingMaterialData

@Database(
    entities= [ProcessingMaterialData::class],
    version = 1,
    exportSchema = false
)
abstract class ProcessingMaterialDataBase : RoomDatabase() {

    abstract fun processingMaterialDao() : ProcessingMDAO

    companion object {
        private var INSTANCE : ProcessingMaterialDataBase? = null

        fun getInstance(context : Context) : ProcessingMaterialDataBase? {
            if(INSTANCE == null) {
                synchronized(ProcessingMaterialDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ProcessingMaterialDataBase::class.java, "processingMaterialTable"
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