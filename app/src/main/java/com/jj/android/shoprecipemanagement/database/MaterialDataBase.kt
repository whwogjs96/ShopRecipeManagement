package com.jj.android.shoprecipemanagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jj.android.shoprecipemanagement.dao.MaterialDAO
import com.jj.android.shoprecipemanagement.dto.MaterialData

@Database(
    entities= [MaterialData::class],
    version = 1,
    exportSchema = false
)
abstract class MaterialDataBase : RoomDatabase() {
    abstract fun materialDao() : MaterialDAO

    companion object {
        private var INSTANCE : MaterialDataBase? = null
        fun getInstance(context : Context) : MaterialDataBase? {
            if(INSTANCE == null) {
                synchronized(MaterialDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MaterialDataBase::class.java, "materialTable"
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