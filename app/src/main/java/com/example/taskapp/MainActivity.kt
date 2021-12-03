package com.example.taskapp

import android.Manifest
import android.app.AppOpsManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.taskapp.data.database.getDatabase
import com.example.taskapp.data.repository.TaskRepository
import com.example.taskapp.databinding.ActivityMainBinding
import com.example.taskapp.ui.tasklist.TaskListViewModel
import com.facebook.stetho.Stetho
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var taskListViewModel: TaskListViewModel

    companion object{
        private const val TAG = "MyActivity"
        private const val JOB_ID_A = 100
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)

        val database = getDatabase(this)
        val repository = TaskRepository(database.taskDao)
        taskListViewModel = ViewModelProvider(this, TaskListViewModel.Factory(repository)).get(
            TaskListViewModel::class.java)

        //権限設定
        if (!checkForPermission()) {
            Log.i(ContentValues.TAG, "The user may not allow the access to apps usage. ")
            Toast.makeText(
                this,
                "Failed to retrieve app usage statistics. " +
                        "You may need to enable access for this app through " +
                        "Settings > Security > Apps with usage access",
                Toast.LENGTH_LONG
            ).show()
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        } else {
            // We have the permission. Query app usage stats.
            val filePath = filesDir.path + "/myText.txt"
            Log.d("Test", "path : $filePath")
        }

        val js = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val serviceComponet = ComponentName(this,MyJobService::class.java)
        val jobInfo = JobInfo.Builder(JOB_ID_A,serviceComponet)
            .setPersisted(true)
            .setPeriodic(24*60*60*1000)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
            .build()
        js.schedule(jobInfo)

    }

    //権限チェック
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkForPermission(): Boolean {
        val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), packageName)
        return mode == AppOpsManager.MODE_ALLOWED
    }
}