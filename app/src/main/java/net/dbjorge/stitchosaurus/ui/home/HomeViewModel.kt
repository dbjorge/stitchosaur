package net.dbjorge.stitchosaurus.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.async
import net.dbjorge.stitchosaurus.data.Project
import net.dbjorge.stitchosaurus.data.ProjectDao

class HomeViewModel(private val projectRepository: ProjectDao) : ViewModel() {
    private val _projectList = MutableLiveData<List<Project>>()
    val projectList: LiveData<List<Project>> get() = _projectList

    fun loadProjects() {
        viewModelScope.async {
            val fetchedProjects = projectRepository.getAll()
            _projectList.postValue(fetchedProjects)
        }
    }
}
