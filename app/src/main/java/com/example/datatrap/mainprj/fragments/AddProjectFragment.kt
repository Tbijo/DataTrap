package com.example.datatrap.mainprj.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.datatrap.databinding.FragmentAddProjectBinding
import com.example.datatrap.models.Project
import com.example.datatrap.viewmodels.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddProjectFragment : Fragment() {

    private var _binding: FragmentAddProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectViewModel: ProjectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddProjectBinding.inflate(inflater, container, false)

        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        // nastavenie onClick na pridanie noveho usera
        binding.btnAddProject.setOnClickListener {
            insertProject()
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun insertProject() {
        val projectName = binding.etProjectName.text.toString()
        if (projectName.isNotEmpty()){

            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())

            val project: Project = Project(projectName, currentDate, 0, 0)

            projectViewModel.insertProject(project)

            Toast.makeText(requireContext(),"New project added.", Toast.LENGTH_SHORT).show()

            val action = AddProjectFragmentDirections.actionAddProjectFragmentToListAllProjectFragment()
            findNavController().navigate(action)
        }else{
            Toast.makeText(requireContext(), "All fields must be filled.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}