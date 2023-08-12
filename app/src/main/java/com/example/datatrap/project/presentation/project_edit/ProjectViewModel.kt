package com.example.datatrap.project.presentation.project_edit

import android.app.AlertDialog
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.datatrap.R
import com.example.datatrap.project.data.Project
import com.example.datatrap.project.presentation.project_list.ProjectListViewModel
import java.util.Calendar

class ProjectViewModel: ViewModel() {

    private val projectListViewModel: ProjectListViewModel by viewModels()

    init {
        initProjectValuesToView()

        when(item.itemId){
            R.id.menu_save -> updateProject()
            R.id.menu_delete -> deleteProject()
        }
    }

    private fun initProjectValuesToView(){
        binding.etProjectName.setText(args.project.projectName)
        binding.etNumLocality.setText(args.project.numLocal.toString())
        binding.etNumMouse.setText(args.project.numMice.toString())
    }

    private fun updateProject() {
        val projectName = binding.etProjectName.text.toString()
        val numLocal = binding.etNumLocality.text.toString()
        val numMice = binding.etNumMouse.text.toString()

        if (checkInput(projectName, numLocal, numMice)){
            val project: Project = args.project.copy()
            project.projectName = projectName
            project.projectDateTimeUpdated = Calendar.getInstance().time
            project.numLocal = Integer.parseInt(numLocal)
            project.numMice = Integer.parseInt(numMice)

            projectListViewModel.updateProject(project)

            Toast.makeText(requireContext(), "Project updated.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(projectName: String, numLocal: String, numMouse: String): Boolean {
        return projectName.isNotEmpty() && numLocal.isNotEmpty() && numMouse.isNotEmpty()
    }

    private fun deleteProject() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            projectListViewModel.deleteProject(args.project)

            Toast.makeText(requireContext(),"Project deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete project?")
            .setMessage("Are you sure you want to delete this project?")
            .create().show()
    }
}