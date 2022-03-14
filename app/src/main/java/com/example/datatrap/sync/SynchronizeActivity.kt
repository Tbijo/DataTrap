package com.example.datatrap.sync

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.ActivitySynchronizeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SynchronizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySynchronizeBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySynchronizeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.sync_fragmentContainerView)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.syncDrawerlayout)
        binding.syncNavigationview.setupWithNavController(navController)
        setupActionBarWithNavController(navController, binding.syncDrawerlayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.sync_fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed(){}
}