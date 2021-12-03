 package com.example.taskapp.ui.tasklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.data.database.getDatabase
import com.example.taskapp.data.repository.TaskRepository
import com.example.taskapp.databinding.FragmentTasklistBinding
import com.example.taskapp.model.Task
import com.example.taskapp.ui.tasklist.list.TaskAdapter
import kotlinx.android.synthetic.main.fragment_tasklist.*

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTasklistBinding
    private lateinit var taskListViewModel : TaskListViewModel
    private lateinit var taskListAdapter:TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tasklist, container, false)
        binding.fragment = this

        binding.apply {
            taskList.adapter = taskListAdapter
        }

        setRecycleView(binding)

        add_task.setOnClickListener {
            taskListViewModel.onButtonClicked(requireContext())
        }

        observeViewModel(taskListViewModel)
        return binding.root

    }

    private fun setRecycleView(binding: FragmentTasklistBinding) {
        val recyclerView = task_list
        //このレイアウトマネージャーとかの定義がないと、RecyclerViewが表示されない
        recyclerView.setHasFixedSize(true); // RecyclerViewのサイズを維持し続ける
        recyclerView.setLayoutManager(LinearLayoutManager(requireContext()));
        recyclerView.setItemAnimator(DefaultItemAnimator());

        // RecyclerView自体の大きさが変わらないことが分かっている時、
        // このオプションを付けておくと、パフォーマンスが改善されるらしい
        recyclerView.setHasFixedSize(true);
    }

    private fun observeViewModel(viewModel: TaskListViewModel) {

        val taskObserver = Observer<List<Task?>?> { tasks ->

            tasks?.let { taskListAdapter.setTaskList(it) }

        }

        viewModel.taskListLiveData.observe(viewLifecycleOwner, taskObserver)

    }
}