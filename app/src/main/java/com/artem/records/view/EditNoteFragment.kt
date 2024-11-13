package com.artem.records.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.artem.records.R
import com.artem.records.databinding.FragmentEditNoteBinding
import com.artem.records.factory.EditNoteViewModelFactory
import com.artem.records.model.database.DiaryDatabase
import com.artem.records.model.repository.NoteRepository
import com.artem.records.viewmodel.NoteEditViewModel

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        val sharedPreferences = requireActivity()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1L).takeIf { it != -1L } ?: run {
            findNavController().navigate(R.id.action_editNoteFragment_to_loginFragment)
            return view
        }

        val noteId = EditNoteFragmentArgs.fromBundle(requireArguments()).noteId

        val application = requireNotNull(this.activity).application
        val dao = DiaryDatabase.getInstance(application).noteDao
        val noteRepository = NoteRepository.getInstance(dao)
        val viewModelFactory = EditNoteViewModelFactory(noteRepository, userId, noteId)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[NoteEditViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigateToNoteList.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_editNoteFragment_to_noteListFragment)
                viewModel.navigatedToNoteList()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
