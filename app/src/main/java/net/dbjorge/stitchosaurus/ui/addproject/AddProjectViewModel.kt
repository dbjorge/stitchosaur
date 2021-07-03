package net.dbjorge.stitchosaurus.ui.addproject

import androidx.lifecycle.*
import kotlinx.coroutines.async
import net.dbjorge.stitchosaurus.data.Project
import net.dbjorge.stitchosaurus.data.ProjectDao

enum class CommitProjectState { NOT_STARTED, IN_PROGRESS, SUCCESS, ERROR }
class AddProjectViewModel(private val projectRepository: ProjectDao) : ViewModel() {
    private val _commitProjectState = MutableLiveData(CommitProjectState.NOT_STARTED)
    val commitProjectState: LiveData<CommitProjectState> get() = _commitProjectState

    fun commitProject(label: String) {
        _commitProjectState.value = CommitProjectState.IN_PROGRESS

        viewModelScope.async {
            val newState = try {
                projectRepository.insert(Project(label))
                CommitProjectState.SUCCESS
            } catch (e: IllegalArgumentException) {
                CommitProjectState.ERROR
            }
            _commitProjectState.postValue(newState)
        }
    }
}
