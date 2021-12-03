package com.example.taskapp.ui.tasklist

import com.example.taskapp.model.Task

interface TaskClickCallback {
    fun onClick(task: Task)
}