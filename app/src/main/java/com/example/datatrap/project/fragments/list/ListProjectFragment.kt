package com.example.datatrap.project.fragments.list

import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListProjectBinding
import com.example.datatrap.models.Project
import com.example.datatrap.viewmodels.ProjectViewModel
import java.text.SimpleDateFormat
import java.util.*

class ListAllProjectFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var adapter: ProjectRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentListProjectBinding.inflate(inflater, container, false)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)

        adapter = ProjectRecyclerAdapter()
        binding.projectRecyclerview.adapter = adapter
        binding.projectRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        projectViewModel.projectList.observe(viewLifecycleOwner, Observer { projects ->
            adapter.setData(projects)
        })

        binding.addProjectFloatButton.setOnClickListener {
            showAddDialog("New Project", "Project Name", "Add new project?")
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchForData(newText)
        }
        return true
    }

    private fun searchForData(query: String?) {
        val searchQuery = "%$query%"

        projectViewModel.searchProjects(searchQuery).observe(viewLifecycleOwner, Observer { projects ->
            projects.let {
                adapter.setData(it)
            }
        })
    }

    private fun showAddDialog(title: String, hint: String, message: String){
        val input = EditText(requireContext())
        input.hint = hint
        input.inputType = InputType.TYPE_CLASS_TEXT

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        .setMessage(message)
        .setView(input)
        .setPositiveButton("OK") { _, _ ->
            val name = input.text.toString()
            insertProject(name)
        }
        .setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        .create().show()
    }

    private fun insertProject(name: String){
        if (name.isNotEmpty()){
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())

            val project: Project = Project(0, name, currentDate, 0, 0)

            projectViewModel.insertProject(project)

            Toast.makeText(requireContext(),"New project added.", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

}