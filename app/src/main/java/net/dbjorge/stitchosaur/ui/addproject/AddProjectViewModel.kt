package net.dbjorge.stitchosaur.ui.addproject

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import net.dbjorge.stitchosaur.data.Project
import net.dbjorge.stitchosaur.data.ProjectDao

class AddProjectViewModel(private val projectRepository: ProjectDao) : ViewModel() {
    enum class State { WAITING_FOR_INPUT, SAVING, SAVE_SUCCESS, SAVE_ERROR }

    private val _state = MutableLiveData(State.WAITING_FOR_INPUT)
    val commitProjectState: LiveData<State> get() = _state

    fun commitProject(label: String) {
        _state.value = State.SAVING

        viewModelScope.launch {
            val newState = try {
                projectRepository.insert(Project(label))
                State.SAVE_SUCCESS
            } catch (e: IllegalArgumentException) {
                State.SAVE_ERROR
            }
            _state.value = newState
        }
    }
}
