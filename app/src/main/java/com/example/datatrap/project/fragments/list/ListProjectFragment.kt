package com.example.datatrap.project.fragments.list

import android.os.Bundle
import android.provider.Settings
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentListProjectBinding
import com.example.datatrap.models.Project
import com.example.datatrap.viewmodels.ProjectViewModel
import com.example.datatrap.viewmodels.UserViewModel
import java.util.*

class ListAllProjectFragment: Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListProjectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: ProjectRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListProjectBinding.inflate(inflater, container, false)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        adapter = ProjectRecyclerAdapter()
        binding.projectRecyclerview.adapter = adapter
        binding.projectRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        projectViewModel.projectList.observe(viewLifecycleOwner, Observer { projects ->
            adapter.setData(projects)
        })

        binding.addProjectFloatButton.setOnClickListener {
            showAddDialog("New Project", "Project Name", "Add new project?")
        }

        // log out
        userViewModel.activeUser.observe(viewLifecycleOwner, {
            if (it != null) {
                it.isActive = 0
                userViewModel.updateUser(it)
                val action = ListAllProjectFragmentDirections.actionListAllProjectFragmentToMainActivity()
                findNavController().navigate(action)
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_project_menu, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> logOut()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        userViewModel.getActiveUser()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchProjects(newText)
        }
        return true
    }

    private fun searchProjects(query: String?) {
        val searchQuery = "%$query%"
        projectViewModel.searchProjects(searchQuery)
            .observe(viewLifecycleOwner, Observer { projects ->
                projects.let {
                    adapter.setData(it)
                }
            })
    }

    private fun showAddDialog(title: String, hint: String, message: String) {
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

    private fun insertProject(name: String) {
        if (name.isNotEmpty()) {
            val deviceID: String = Settings.Secure.getString(
                requireContext().contentResolver,
                Settings.Secure.ANDROID_ID
            )
            val project: Project = Project(0, name, deviceID, Calendar.getInstance().time, null, 0, 0)

            projectViewModel.insertProject(project)

            Toast.makeText(requireContext(), "New project added.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

}