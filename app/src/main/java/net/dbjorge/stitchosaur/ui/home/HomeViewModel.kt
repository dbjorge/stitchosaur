package net.dbjorge.stitchosaur.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.async
import net.dbjorge.stitchosaur.data.Project
import net.dbjorge.stitchosaur.data.ProjectDao

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
