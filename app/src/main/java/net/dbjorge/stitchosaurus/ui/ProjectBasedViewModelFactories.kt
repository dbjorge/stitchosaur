package net.dbjorge.stitchosaurus.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.dbjorge.stitchosaurus.StitchosaurusApplication
import net.dbjorge.stitchosaurus.data.ProjectDao

class ProjectBasedViewModelFactory(fragment: Fragment) : ViewModelProvider.Factory {
    private val projectRepository: ProjectDao =
        (fragment.activity?.application as StitchosaurusApplication).database.projectDao();

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getDeclaredConstructor(ProjectDao::class.java).newInstance(projectRepository)
    }
}
