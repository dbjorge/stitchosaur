package net.dbjorge.stitchosaurus.ui.deleteproject

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.dbjorge.stitchosaurus.data.Project
import net.dbjorge.stitchosaurus.data.ProjectDao

private const val TAG = "DeleteProjectViewModel"

class DeleteProjectViewModel(private val projectRepository: ProjectDao) : ViewModel() {
    enum class State { LOADING, WAITING_FOR_INPUT, DELETING, DELETE_SUCCESS, DELETE_ERROR }

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

    fun confirmDelete() {
        _state.value = State.DELETING
        viewModelScope.launch {
            _state.value = try {
                projectRepository.delete(project.value!!)
                State.DELETE_SUCCESS
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting project ${project.value?.id}", e)
                State.DELETE_ERROR
            }
        }
    }
}
