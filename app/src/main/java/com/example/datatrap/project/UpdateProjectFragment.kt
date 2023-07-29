package com.example.datatrap.project

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateProjectBinding
import com.example.datatrap.project.data.Project
import com.example.datatrap.project.presentation.ProjectViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UpdateProjectFragment : Fragment() {

    private var _binding: FragmentUpdateProjectBinding? = null
    private val binding get() = _binding!!
    private val projectViewModel: ProjectViewModel by viewModels()
    private val args by navArgs<UpdateProjectFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentUpdateProjectBinding.inflate(inflater, container, false)

        initProjectValuesToView()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateProject()
            R.id.menu_delete -> deleteProject()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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

            projectViewModel.updateProject(project)

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

            projectViewModel.deleteProject(args.project)

            Toast.makeText(requireContext(),"Project deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete project?")
            .setMessage("Are you sure you want to delete this project?")
            .create().show()
    }

}