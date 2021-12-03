package com.example.taskapp.ui.tasklist.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.databinding.TaskItemBinding
import com.example.taskapp.model.Task
import com.example.taskapp.ui.tasklist.TaskClickCallback

class TaskAdapter(private val taskClickCallback: TaskClickCallback?) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var taskList: List<Task?>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setTaskList(taskList: List<Task?>?) {

        this.taskList = taskList

        //これ大事。ないと、データ追加後に画面が更新されません。
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): TaskViewHolder {
        val binding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.task_item, parent,
                false
            ) as TaskItemBinding


        return TaskViewHolder(binding)
    }

    open class TaskViewHolder(val binding: TaskItemBinding ) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        holder.binding.task = taskList?.get(position) //task_list_item.xmlの中のtask
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = taskList?.size?:0

}