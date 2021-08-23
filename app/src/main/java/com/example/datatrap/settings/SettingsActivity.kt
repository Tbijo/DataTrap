package com.example.datatrap.settings

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
import com.example.datatrap.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.settings_fragmentContainerView)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.settingsDrawerlayout)
        binding.settingsNavigationview.setupWithNavController(navController)
        setupActionBarWithNavController(navController, binding.settingsDrawerlayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.settings_fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed(){}
}