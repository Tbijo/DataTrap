package com.example.datatrap.project.presentation.project_list

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.project.data.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
): ViewModel() {

    private val projectListViewModel: ProjectListViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    private lateinit var projectEntityList: List<ProjectEntity>

    init {
        holder.binding.tvProjectDate.text = SimpleDateFormat.getDateTimeInstance().format(Date(currenItem.projectStart))

        projectListViewModel.projectEntityList.observe(viewLifecycleOwner) { projects ->
            projectEntityList = projects
            adapter.setData(projects)
        }

        adapter.setOnItemClickListener(object : ProjectRecyclerAdapter.MyClickListener {

            override fun useClickListener(position: Int) {
                val project = projectEntityList[position]
                // nastavit meno Projektu na zobrazenie
                prefViewModel.savePrjNamePref(project.projectName)
                // tu sa prejde na locality s projektom
                val action = ListAllProjectFragmentDirections.actionListAllProjectFragmentToListPrjLocalityFragment(project)
                findNavController().navigate(action)
            }

            override fun useLongClickListener(position: Int) {
                // tu sa prejde na upravu vybraneho projektu
                val project = projectEntityList[position]
                val action = ListAllProjectFragmentDirections.actionListAllProjectFragmentToUpdateProjectFragment(project)
                findNavController().navigate(action)
            }

        })

        binding.addProjectFloatButton.setOnClickListener {
            showAddDialog("New Project", "Project Name", "Add new project?")
        }

        R.id.menu_logout -> logOut()
    }

    private fun logOut() {
        val action = ListAllProjectFragmentDirections.actionListAllProjectFragmentToMainActivity()
        findNavController().navigate(action)
    }

    private fun searchProjects(query: String?) {
        val searchQuery = "%$query%"
        projectListViewModel.searchProjects(searchQuery)
            .observe(viewLifecycleOwner) { projects ->
                projects.let {
                    adapter.setData(it)
                }
            }
    }

    private fun insertProject(name: String) {
        if (name.isNotEmpty()) {

            val projectEntity: ProjectEntity = ProjectEntity(0, name, Calendar.getInstance().time, null, 0, 0, Calendar.getInstance().time.time)

            projectListViewModel.insertProject(projectEntity)

            Toast.makeText(requireContext(), "New project added.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchProjects(newText)
        }
        return true
    }

    val projectEntityList: LiveData<List<ProjectEntity>> = projectRepository.projectEntityList

    fun insertProject(projectEntity: ProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.insertProject(projectEntity)
        }
    }

    fun updateProject(projectEntity: ProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.updateProject(projectEntity)
        }
    }

    fun deleteProject(projectEntity: ProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            projectRepository.deleteProject(projectEntity)
        }
    }

    fun searchProjects(projectName: String): LiveData<List<ProjectEntity>> {
        return projectRepository.searchProjects(projectName)
    }
}