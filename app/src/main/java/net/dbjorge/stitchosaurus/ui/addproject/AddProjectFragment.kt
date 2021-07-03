package net.dbjorge.stitchosaurus.ui.addproject

import android.os.Bundle
import android.util.Log
import android.util.Log.VERBOSE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import net.dbjorge.stitchosaurus.R
import net.dbjorge.stitchosaurus.databinding.AddProjectFragmentBinding
import net.dbjorge.stitchosaurus.ui.ProjectBasedViewModelFactory
import net.dbjorge.stitchosaurus.ui.closeSoftKeyboard

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

    private fun renderState(state: CommitProjectState) {
        when (state) {
            CommitProjectState.SUCCESS -> {
                Log.v(TAG, "Successfully added project, returning to previous fragment")
                findNavController().popBackStack()
            }
            CommitProjectState.ERROR -> {
                binding.projectLabelEditText.error = getString(R.string.error_validating_project_label)
            }
            else -> { }
        }
    }
}