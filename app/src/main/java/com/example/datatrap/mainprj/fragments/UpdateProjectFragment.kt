package com.example.datatrap.mainprj.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentUpdateProjectBinding
import com.example.datatrap.models.Project
import com.example.datatrap.viewmodels.ProjectViewModel

class UpdateProjectFragment : Fragment() {

    private var _binding: FragmentUpdateProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectViewModel: ProjectViewModel
    private val args by navArgs<UpdateProjectFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentUpdateProjectBinding.inflate(inflater, container, false)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        binding.etProjectName.setText(args.project.projectName)
        binding.etDate.setText(args.project.date)
        binding.etNumLocality.setText(args.project.numLocal)
        binding.etNumMouse.setText(args.project.numMice)

        binding.btnUpdateProject.setOnClickListener {
            updateProject()
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete -> deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateProject() {
        val projectName = binding.etProjectName.text.toString()
        val date = binding.etDate.text.toString()
        val numLocal = binding.etNumLocality.text.toString()
        val numMouse = binding.etNumMouse.text.toString()

        if (projectName.isNotEmpty() && date.isNotEmpty() && numLocal.isNotEmpty() && numMouse.isNotEmpty()){
            val project = Project(projectName, date, Integer.parseInt(numLocal), Integer.parseInt(numMouse))
            projectViewModel.updateProject(project)
            Toast.makeText(requireContext(), "Project updated.", Toast.LENGTH_SHORT).show()

            val action = UpdateProjectFragmentDirections.actionUpdateProjectFragmentToListAllProjectFragment()
            findNavController().navigate(action)
        }else{
            Toast.makeText(requireContext(), "All fields must be filled.", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            projectViewModel.deleteProject(args.project)

            Toast.makeText(requireContext(),"Project deleted.", Toast.LENGTH_LONG).show()
            val action = UpdateProjectFragmentDirections.actionUpdateProjectFragmentToListAllProjectFragment()
            findNavController().navigate(action)
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete project?")
            .setMessage("Are you sure you want to delete this project?")
            .create().show()
    }

}