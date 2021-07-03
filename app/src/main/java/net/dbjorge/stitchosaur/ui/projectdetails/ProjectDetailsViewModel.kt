package net.dbjorge.stitchosaur.ui.projectdetails

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.dbjorge.stitchosaur.data.Project
import net.dbjorge.stitchosaur.data.ProjectDao

private const val TAG = "ProjectDetailsViewModel"

class ProjectDetailsViewModel(private val projectRepository: ProjectDao) : ViewModel() {
    private val _project = MutableLiveData<Project>()
    val project: LiveData<Project> get() = _project

    val isLoaded: LiveData<Boolean> = _project.map { p -> p != null }.distinctUntilChanged()
    val notes: LiveData<String> = _project.map { p -> p?.notes ?: "" }.distinctUntilChanged()
    val label: LiveData<String> = _project.map { p -> p?.label ?: "" }.distinctUntilChanged()
    val counter: LiveData<Int> = _project.map { p -> p?.counter ?: 0 }.distinctUntilChanged()

    fun incrementCounter() = updateProject { p -> p.counter++ }
    fun resetCounter() = updateProject { p -> p.counter = 0 }
    fun updateNotes(newNotes: String?) = updateProject { p -> p.notes = newNotes ?: "" }

    fun loadProject(projectId: Int) {
        Log.v(TAG, "Starting load of project $projectId")
        viewModelScope.launch {
            val fetchedProject = projectRepository.getById(projectId)
            _project.value = fetchedProject
            Log.v(TAG, "Successfully loaded project ${fetchedProject.id}")
        }
    }

    private fun updateProject(updateOperation: (project: Project) -> Unit) {
        if (_project.value == null) {
            Log.w(TAG, "Ignoring request to update project before it is loaded")
            return
        }

        val updatedProject = _project.value!!.copy().also(updateOperation)
        if (updatedProject == _project.value) {
            Log.v(TAG, "Ignoring no-op updateProject request")
            return
        }

        _project.value = updatedProject
        viewModelScope.launch {
            projectRepository.update(_project.value!!)
            Log.v(TAG, "Successfully updated project ${project.value?.id}")
        }
    }
}
