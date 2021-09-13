package com.jj.android.shoprecipemanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jj.android.shoprecipemanagement.dao.ProcessingMDetailDAO
import com.jj.android.shoprecipemanagement.dto.ProcessingMDetailData

@Database(
    entities= [ProcessingMDetailData::class],
    version = 1,
    exportSchema = false
)
abstract class ProcessingMDetailDataBase : RoomDatabase() {

    abstract fun processingMDetailDao() : ProcessingMDetailDAO

    companion object {
        private var INSTANCE : ProcessingMDetailDataBase? = null

        fun getInstance(context : Context) : ProcessingMDetailDataBase? {
            if(INSTANCE == null) {
                synchronized(ProcessingMDetailDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        ProcessingMDetailDataBase::class.java, "processingMDetailTable"
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