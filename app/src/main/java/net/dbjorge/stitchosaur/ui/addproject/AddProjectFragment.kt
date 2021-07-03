package net.dbjorge.stitchosaur.ui.addproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import net.dbjorge.stitchosaur.R
import net.dbjorge.stitchosaur.databinding.AddProjectFragmentBinding
import net.dbjorge.stitchosaur.ui.ProjectBasedViewModelFactory
import net.dbjorge.stitchosaur.ui.closeSoftKeyboard

private const val TAG = "AddProjectFragment"

class AddProjectFragment : Fragment() {
    private val viewModel: AddProjectViewModel by viewModels {
        ProjectBasedViewModelFactory(this)
    }

    private var _binding: AddProjectFragmentBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddProjectFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.commitProjectState.observe(viewLifecycleOwner, this::renderState)

        binding.projectLabelEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.commitProject(v.text.toString())
                v.closeSoftKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun renderState(state: AddProjectViewModel.State) {
        when (state) {
            AddProjectViewModel.State.SAVE_SUCCESS -> {
                Log.v(TAG, "Successfully added project, returning to previous fragment")
                findNavController().popBackStack()
            }
            AddProjectViewModel.State.SAVE_ERROR -> {
                binding.projectLabelEditText.error = getString(R.string.error_validating_project_label)
            }
            else -> { }
        }
    }
}