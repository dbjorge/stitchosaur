package net.dbjorge.stitchosaur.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import net.dbjorge.stitchosaur.data.Project
import net.dbjorge.stitchosaur.databinding.HomeFragmentBinding
import net.dbjorge.stitchosaur.ui.ProjectBasedViewModelFactory

private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels {
        ProjectBasedViewModelFactory(this)
    }

    private val clickListener: ClickListener = this::onProjectClicked
    private val recyclerViewAdapter = ProjectRecyclerViewAdapter(clickListener)

    private var _binding: HomeFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        Log.v(TAG, "Reloading projects")
        viewModel.loadProjects()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        this.viewModel.projectList.observe(viewLifecycleOwner, { projects ->
            projects?.let { render(projects) }
        })

        binding.addProjectFab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeToAddProject()
            findNavController().navigate(action)
        }
    }

    private fun render(projectList: List<Project>) {
        Log.v(TAG, "Updating projectList with ${projectList.size} projects")
        recyclerViewAdapter.updateProjects(projectList)
        if (projectList.isEmpty()) {
            binding.projectsRecyclerView.visibility = View.GONE
            binding.noProjectsTextView.visibility = View.VISIBLE
        } else {
            binding.projectsRecyclerView.visibility = View.VISIBLE
            binding.noProjectsTextView.visibility = View.GONE
        }
    }

    private fun onProjectClicked(project: Project) {
        val action = HomeFragmentDirections.actionHomeToProjectDetails(project.id)
        findNavController().navigate(action)
    }

    private fun setupRecyclerView() {
        binding.projectsRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.projectsRecyclerView.adapter = recyclerViewAdapter
        binding.projectsRecyclerView.setHasFixedSize(true)
    }
}