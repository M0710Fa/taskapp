package com.example.taskapp.ui.tasklist

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.taskapp.data.repository.TaskRepository
import com.example.taskapp.model.Task
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TaskListViewModel constructor(private val repository: TaskRepository) : ViewModel() {

    val TAG: String = "TaskListViewModel"
    var taskListLiveData = MutableLiveData<List<Task>>()

    init {
        loadTaskList()
    }


    private fun loadTaskList() {
        viewModelScope.launch {
            try {

                Log.d(TAG, "viewModelScope.launch ")
                val listNotLiveData = repository.getTaskList()

                // メンバー変数のLiveDataにこの値を送っている　これが大事！！ここは、LiveDataを送るのではなく、リスト形式を送るのがミソ
                taskListLiveData.postValue(listNotLiveData)


            } catch (e: Exception) {

                Log.e(TAG, "データ取得中にエラー " + e)

            }
        }
    }

    fun onButtonClicked(context: Context) {

        //ダミーのタスクリスト
        val task1 = Task(null,"掃除する",null,"2021/10/10","2021/11/10");

        var task_array = ArrayList<Task>()
        task_array.add(task1)

        viewModelScope.launch {

            val index = Random().nextInt(task_array.size) // ランダムに選択された 0 以上 4 未満の整数
            val result = task_array.get(index)
            addTaskDb(context, result)

            //これはやらないくてもいいのかと思ってましたが、タスクリストをリフレッシュして表示するのに必要です。
            loadTaskList()
        }
    }


    suspend fun addTaskDb(context: Context, task: Task) {
        try {

            repository.addTask(context, task)

        } catch (cause: Throwable) {
            // If anything throws an exception, inform the caller
            Log.e("TaskViewModel", "エラー　データベースでデータ追加できない")
            Toast.makeText(context, "データベースエラーで更新できませんでした!", Toast.LENGTH_LONG).show();
        }
    }



    //引数が必要な時は、Factoryが必要
    class Factory(private val repository: TaskRepository) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskListViewModel(repository) as T
        }
    }

}