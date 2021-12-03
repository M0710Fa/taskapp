package com.example.taskapp.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.taskapp.data.dao.TaskDao
import com.example.taskapp.model.Task

class TaskRepository(val taskDao: TaskDao) {
    suspend fun getTaskList(): List<Task> {

        try {

            val list = taskDao.taskListNotLiveData()
            return list


        } catch (cause: Throwable) {

            Log.e("TaskRepository", cause.toString())
            val list = emptyList<Task>()
            return list
        }


    }

    suspend fun addTask(context: Context, task: Task) {

        try {

            taskDao.insertOne(task)

        } catch (cause: Throwable) {

            Log.e("TaskRepository", cause.toString())
            Toast.makeText(context, "task_cannot_create_db_error" + cause.toString(), Toast.LENGTH_LONG).show();

        }
    }

}