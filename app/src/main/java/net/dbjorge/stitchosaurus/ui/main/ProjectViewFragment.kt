package net.dbjorge.stitchosaurus.ui.main

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.dbjorge.stitchosaurus.databinding.FragmentMainBinding

class ProjectViewFragment : Fragment() {

    private lateinit var projectViewModel: ProjectViewModel
    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java).apply {
            setProjectId(arguments?.getInt(ARG_PROJECT_ID) ?: -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        projectViewModel.notes.observe(viewLifecycleOwner, {
            binding.notesTextView.text = it
        })
        projectViewModel.counter.observe(viewLifecycleOwner, {
            binding.counterButton.text = it.toString()
        })

        binding.counterButton.setOnClickListener { projectViewModel.incrementCounter() }
        binding.resetCounterButton.setOnClickListener { projectViewModel.resetCounter() }
        binding.notesTextView.doAfterTextChanged { text -> projectViewModel.updateNotes(text?.toString()) }

        return root
    }

    companion object {
        private const val ARG_PROJECT_ID = "project_id"

        @JvmStatic
        fun newInstance(sectionNumber: Int): ProjectViewFragment {
            return ProjectViewFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PROJECT_ID, sectionNumber)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}