package net.dbjorge.stitchosaurus.ui.projectdetails

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import net.dbjorge.stitchosaurus.R
import net.dbjorge.stitchosaurus.databinding.ProjectDetailsFragmentBinding
import net.dbjorge.stitchosaurus.ui.ProjectBasedViewModelFactory
import net.dbjorge.stitchosaurus.ui.home.HomeFragmentDirections

class ProjectDetailsFragment : Fragment() {
    private val args by navArgs<ProjectDetailsFragmentArgs>()

    private val viewModel: ProjectDetailsViewModel by viewModels {
        ProjectBasedViewModelFactory(this)
    }

    private var _binding: ProjectDetailsFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProjectDetailsFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_project_details_options, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editLabel -> {
                val action = ProjectDetailsFragmentDirections.actionProjectDetailsToEditProject(args.projectId)
                findNavController().navigate(action)
                true
            }
            R.id.deleteProject -> {
                val action = ProjectDetailsFragmentDirections.actionProjectDetailsToDeleteProject(args.projectId)
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProject(args.projectId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoaded.observe(viewLifecycleOwner, {
            binding.loadingSpinner.visibility = if(it) View.GONE else View.VISIBLE
            binding.contentLayout.visibility = if(it) View.VISIBLE else View.GONE
        })
        viewModel.label.observe(viewLifecycleOwner, {
            (requireActivity() as AppCompatActivity).supportActionBar?.title = it
        })
        viewModel.notes.observe(viewLifecycleOwner, {
            if (!binding.notesEditText.text.contentEquals(it)) {
                binding.notesEditText.setText(it)
            }
        })
        viewModel.counter.observe(viewLifecycleOwner, {
            binding.counterButton.text = it.toString()
        })

        binding.counterButton.setOnClickListener { viewModel.incrementCounter() }
        binding.resetCounterButton.setOnClickListener { viewModel.resetCounter() }
        binding.notesEditText.doAfterTextChanged { text -> viewModel.updateNotes(text?.toString()) }
    }
}