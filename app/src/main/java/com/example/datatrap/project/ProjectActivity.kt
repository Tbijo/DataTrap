package com.example.datatrap.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.ActivityProjectBinding
import com.example.datatrap.myinterfaces.OnActiveFragment

class ProjectActivity : AppCompatActivity(), OnActiveFragment {

    private lateinit var binding: ActivityProjectBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.project_fragmentContainerView)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.projectDrawerlayout)
        binding.projectNavigationview.setupWithNavController(navController)
        setupActionBarWithNavController(navController, binding.projectDrawerlayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.project_fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //override fun onBackPressed(){}

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }
}