package com.artem.records.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.artem.records.model.dao.NoteDao
import com.artem.records.model.dao.UserDao
import com.artem.records.model.entity.Note
import com.artem.records.model.entity.User

@Database(entities = [User::class, Note::class], version = 2, exportSchema = false)
abstract class DiaryDatabase: RoomDatabase() {

    abstract val userDao: UserDao
    abstract val noteDao: NoteDao

    companion object {

        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        fun getInstance(context: Context): DiaryDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DiaryDatabase::class.java,
                        "diary_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}
