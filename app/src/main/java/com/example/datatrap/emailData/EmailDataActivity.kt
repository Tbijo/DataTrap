package com.example.datatrap.emailData

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.ActivityEmailDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailDataActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailDataBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.emaildata_fragmentContainerView)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.emaildataDrawerlayout)
        binding.emaildataNavigationview.setupWithNavController(navController)
        setupActionBarWithNavController(navController, binding.emaildataDrawerlayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.emaildata_fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed(){}
}