package net.dbjorge.stitchosaur.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.dbjorge.stitchosaur.StitchosaurApplication
import net.dbjorge.stitchosaur.data.ProjectDao

class ProjectBasedViewModelFactory(fragment: Fragment) : ViewModelProvider.Factory {
    private val projectRepository: ProjectDao =
        (fragment.activity?.application as StitchosaurApplication).database.projectDao();

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getDeclaredConstructor(ProjectDao::class.java).newInstance(projectRepository)
    }
}
