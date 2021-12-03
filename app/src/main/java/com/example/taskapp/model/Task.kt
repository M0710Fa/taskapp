package com.example.taskapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = 0,
    var content : String?,
    var limit: String?,
    var created: String,
    var required_time: String
){
    override fun toString(): String {
        val task_name:String
        if(content != null){
            task_name = content!!
        }else{
            task_name = "無題"
        }

        return task_name
    }
}
