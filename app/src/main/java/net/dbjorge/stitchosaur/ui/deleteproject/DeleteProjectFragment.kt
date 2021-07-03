package net.dbjorge.stitchosaur.ui.deleteproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import net.dbjorge.stitchosaur.R
import net.dbjorge.stitchosaur.data.Project
import net.dbjorge.stitchosaur.databinding.DeleteProjectFragmentBinding
import net.dbjorge.stitchosaur.ui.ProjectBasedViewModelFactory

private const val TAG = "DeleteProjectFragment"

class DeleteProjectFragment : Fragment() {
    private val args by navArgs<DeleteProjectFragmentArgs>()

    private val viewModel: DeleteProjectViewModel by viewModels {
        ProjectBasedViewModelFactory(this)
    }

    private var _binding: DeleteProjectFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DeleteProjectFragmentBinding.inflate(inflater, container, false)
        return binding.root
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
        viewModel.state.observe(viewLifecycleOwner, this::renderState)
        viewModel.project.observe(viewLifecycleOwner, this::renderProject)
        binding.confirmDeleteButton.setOnClickListener { viewModel.confirmDelete() }
        binding.cancelDeleteButton.setOnClickListener {
            Log.v(TAG, "Canceling deletion, returning to previous fragment")
            findNavController().popBackStack()
        }
    }

    private fun renderProject(project: Project?) {
        binding.confirmationMessageText.text = if (project == null) "" else getString(R.string.delete_confirmation_message, project.label)
    }

    private fun renderState(state: DeleteProjectViewModel.State) {
        binding.confirmDeleteButton.isEnabled = (state == DeleteProjectViewModel.State.WAITING_FOR_INPUT)

        when (state) {
            DeleteProjectViewModel.State.LOADING -> {
                binding.confirmDeleteButton.isEnabled = false
            }
            DeleteProjectViewModel.State.WAITING_FOR_INPUT -> {
                binding.confirmDeleteButton.isEnabled = true
            }
            DeleteProjectViewModel.State.DELETING -> {
                binding.confirmDeleteButton.isEnabled = false
            }
            DeleteProjectViewModel.State.DELETE_SUCCESS -> {
                Log.v(TAG, "Successfully deleted project, returning to grandparent fragment")
                findNavController().popBackStack(R.id.projectDetailsFragment, true)
            }
            DeleteProjectViewModel.State.DELETE_ERROR -> {
                Snackbar.make(binding.root, R.string.error_deleting_project, Snackbar.LENGTH_LONG).show()
                binding.confirmDeleteButton.isEnabled = true
            }
        }
    }
}