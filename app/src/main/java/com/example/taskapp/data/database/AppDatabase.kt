package com.example.taskapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskapp.data.dao.TaskDao
import com.example.taskapp.model.Task

@Database(entities = [Task::class],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao : TaskDao
}

private lateinit var INSTANCE: AppDatabase
/**
 * Instantiate a database from a context.
 */
fun getDatabase(context: Context): AppDatabase {
    synchronized(AppDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task-admin"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}