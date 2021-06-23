package net.dbjorge.stitchosaurus.ui.main

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import net.dbjorge.stitchosaurus.R
import net.dbjorge.stitchosaurus.data.Project
import net.dbjorge.stitchosaurus.data.ProjectDao

class ProjectViewModel(private val projectRepository: ProjectDao) : ViewModel() {
    private lateinit var _project: LiveData<Project>

    fun setProjectId(projectId: Int) {
        _project = projectRepository.getById(projectId).asLiveData()
    }
    private fun updateProject(updateOperation: (project: Project) -> Unit) = viewModelScope.launch {
        val project = _project.value!!
        updateOperation(project)
        projectRepository.update(project);
    }

    val notes: LiveData<String> = _project.map { p -> p.notes }
    val label: LiveData<String> = _project.map { p -> p.label }
    val counter: LiveData<Int> = _project.map { p -> p.counter }

    fun incrementCounter() = updateProject { p -> p.counter++ }
    fun resetCounter() = updateProject { p -> p.counter = 0 }
    fun renameProject(newLabel: String) = updateProject { p -> p.label = newLabel }
    fun updateNotes(newNotes: String?) = updateProject { p -> p.notes = newNotes ?: "" }
}