package com.example.datatrap.specie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.datatrap.R
import com.example.datatrap.databinding.ActivitySpecieBinding

class SpecieActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySpecieBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpecieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.specie_fragmentContainerView)
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.specieDrawerlayout)
        binding.specieNavigationview.setupWithNavController(navController)
        setupActionBarWithNavController(navController, binding.specieDrawerlayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.specie_fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //override fun onBackPressed(){}
}