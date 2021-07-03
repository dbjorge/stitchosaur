package net.dbjorge.stitchosaur.ui.editproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import net.dbjorge.stitchosaur.R
import net.dbjorge.stitchosaur.databinding.EditProjectFragmentBinding
import net.dbjorge.stitchosaur.ui.ProjectBasedViewModelFactory
import net.dbjorge.stitchosaur.ui.closeSoftKeyboard

private const val TAG = "EditProjectFragment"

class EditProjectFragment : Fragment() {
    private val args by navArgs<EditProjectFragmentArgs>()

    private val viewModel: EditProjectViewModel by viewModels {
        ProjectBasedViewModelFactory(this)
    }

    private var _binding: EditProjectFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditProjectFragmentBinding.inflate(inflater, container, false)
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

        binding.projectLabelEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.saveProject(v.text.toString())
                v.closeSoftKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun renderState(state: EditProjectViewModel.State) {
        when (state) {
            EditProjectViewModel.State.SAVE_SUCCESS -> {
                Log.v(TAG, "Successfully saved project, returning to previous fragment")
                findNavController().popBackStack()
            }
            EditProjectViewModel.State.SAVE_ERROR -> {
                binding.projectLabelEditText.error = getString(R.string.error_validating_project_label)
                binding.projectLabelEditText.isEnabled = true
            }
            EditProjectViewModel.State.WAITING_FOR_INPUT -> {
                binding.projectLabelEditText.setText(viewModel.project.value?.label ?: "")
                binding.projectLabelEditText.isEnabled = true
            }
            else -> {
                binding.projectLabelEditText.isEnabled = false
            }
        }
    }
}