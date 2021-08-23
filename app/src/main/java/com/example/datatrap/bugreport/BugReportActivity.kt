package com.example.datatrap.bugreport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.ActivityBugReportBinding

class BugReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBugReportBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBugReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.bugreport_fragmentContainerView)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.bugreportDrawerlayout)
        binding.bugreportNavigationview.setupWithNavController(navController)
        setupActionBarWithNavController(navController, binding.bugreportDrawerlayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.bugreport_fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed(){}
}