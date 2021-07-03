package net.dbjorge.stitchosaurus.ui.editproject

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.dbjorge.stitchosaurus.data.Project
import net.dbjorge.stitchosaurus.data.ProjectDao
import net.dbjorge.stitchosaurus.ui.deleteproject.DeleteProjectViewModel

private const val TAG = "EditProjectViewModel"

class EditProjectViewModel(private val projectRepository: ProjectDao) : ViewModel() {
    enum class State { LOADING, WAITING_FOR_INPUT, SAVING, SAVE_SUCCESS, SAVE_ERROR }

    private val _state = MutableLiveData(State.LOADING)
    val state: LiveData<State> get() = _state

    private val _project = MutableLiveData<Project>()
    val project: LiveData<Project?> get() = _project

    fun loadProject(projectId: Int) {
        _state.value = State.LOADING
        viewModelScope.launch {
            val fetchedProject = projectRepository.getById(projectId)
            _project.value = fetchedProject
            _state.value = State.WAITING_FOR_INPUT
        }
    }

    fun saveProject(newLabel: String) {
        _state.value = State.SAVING
        viewModelScope.launch {
            _state.value = try {
                val updatedProject = project.value!!.also { p -> p.label = newLabel }
                projectRepository.update(updatedProject)
                Log.v(TAG, "Successfully updated project ${project.value?.id}")
                State.SAVE_SUCCESS
            } catch (e: Exception) {
                Log.e(TAG, "Error saving project ${project.value?.id}", e)
                State.SAVE_SUCCESS
            }
        }
    }
}
