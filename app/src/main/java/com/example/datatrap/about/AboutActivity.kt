package com.example.datatrap.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.about_fragmentContainerView)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.aboutDrawerlayout)
        binding.aboutNavigationview.setupWithNavController(navController)
        setupActionBarWithNavController(navController, binding.aboutDrawerlayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.about_fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed(){}
}