package com.artem.records.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.artem.records.R
import com.artem.records.adapter.NoteItemAdapter
import com.artem.records.databinding.FragmentNoteListBinding
import com.artem.records.factory.NoteListViewModelFactory
import com.artem.records.model.database.DiaryDatabase
import com.artem.records.model.repository.NoteRepository
import com.artem.records.viewmodel.NoteListViewModel

class NoteListFragment : Fragment() {

    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteListBinding.inflate(inflater, container, false)
        val view = binding.root

        val sharedPreferences = requireActivity()
            .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("user_id", -1L).takeIf { it != -1L } ?: run {
            findNavController().navigate(R.id.action_noteListFragment_to_loginFragment)
            return view
        }

        val application = requireNotNull(this.activity).application
        val dao = DiaryDatabase.getInstance(application).noteDao
        val noteRepository = NoteRepository.getInstance(dao)
        val viewModelFactory = NoteListViewModelFactory(noteRepository, userId)
        val viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[NoteListViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = NoteItemAdapter (
            onEditClickListener = { noteId ->
                viewModel.onEditButtonClicked(noteId)
            },
            onDeleteClickListener = {  note ->
                showDeleteConfirmationDialog {
                    viewModel.onDeleteButtonClicked(note)
                }
            }
        )

        binding.noteList.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.noteList.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.noteList.visibility = View.VISIBLE
            }
        }

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            notes?.let {
                adapter.submitList(it)
            }
        }

        viewModel.navigateToEditNote.observe(viewLifecycleOwner) { noteId ->
            noteId?.let {
                val action = NoteListFragmentDirections
                    .actionNoteListFragmentToEditNoteFragment(noteId)
                findNavController().navigate(action)
                viewModel.navigatedToEdit()
            }
        }

        viewModel.navigateToCreateNote.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                val action = NoteListFragmentDirections
                    .actionNoteListFragmentToEditNoteFragment(-1L)
                findNavController().navigate(action)
                viewModel.navigatedToCreate()
            }
        }

        setupSwipeToDeleteAndEdit(binding.noteList, adapter, viewModel)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSwipeToDeleteAndEdit(
        recyclerView: RecyclerView,
        adapter: NoteItemAdapter,
        viewModel: NoteListViewModel
    ) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            private val deleteGradient = GradientDrawable(
                GradientDrawable.Orientation.RIGHT_LEFT,
                intArrayOf(
                    ContextCompat.getColor(requireContext(), R.color.background_color),
                    ContextCompat.getColor(requireContext(), R.color.accent_color_red_light)
                )
            )
            private val editGradient = GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                intArrayOf(
                    ContextCompat.getColor(requireContext(), R.color.background_color),
                    ContextCompat.getColor(requireContext(), R.color.accent_color_blue_light)
                )
            )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = adapter.currentList[position]

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        adapter.notifyItemChanged(viewHolder.adapterPosition)

                        showDeleteConfirmationDialog {
                            viewModel.onDeleteButtonClicked(note)
                        }
                    }
                    ItemTouchHelper.RIGHT -> {
                        viewModel.onEditButtonClicked(note.noteId)
                    }
                }
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconMargin = resources.getDimension(R.dimen.icon_margin).toInt()
                val iconSize = resources.getDimension(R.dimen.icon_size).toInt()
                val iconTop = itemView.top + (itemView.height - iconSize) / 2
                val iconBottom = iconTop + iconSize

                val cornerRadius = resources.getDimension(R.dimen.card_corner_radius).toInt()
                val swipeHeight = itemView.height - 2 * cornerRadius
                val swipeTop = itemView.top + cornerRadius
                val swipeBottom = swipeTop + swipeHeight

                if (dX > 0) {
                    editGradient.setBounds(
                        itemView.left,
                        swipeTop,
                        itemView.left + dX.toInt(),
                        swipeBottom
                    )
                    editGradient.draw(c)
                    val editIcon = ContextCompat.getDrawable(requireContext(), R.drawable.pencil)
                    editIcon?.setBounds(
                        itemView.left + iconMargin,
                        iconTop,
                        itemView.left + iconMargin + iconSize,
                        iconBottom
                    )
                    editIcon?.draw(c)
                } else if (dX < 0) {
                    deleteGradient.setBounds(
                        itemView.right + dX.toInt(),
                        swipeTop,
                        itemView.right,
                        swipeBottom
                    )
                    deleteGradient.draw(c)
                    val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.trash)
                    deleteIcon?.setBounds(
                        itemView.right - iconMargin - iconSize,
                        iconTop,
                        itemView.right - iconMargin,
                        iconBottom
                    )
                    deleteIcon?.draw(c)
                }
                super.onChildDraw(c, recyclerView, viewHolder,
                    dX, dY, actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

    private fun showDeleteConfirmationDialog(onDeleteListener: () -> Unit) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_confirm_delete, null)
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialogView.findViewById<Button>(R.id.positiveButton).setOnClickListener {
            onDeleteListener()
            alertDialog.dismiss()
        }

        dialogView.findViewById<Button>(R.id.negativeButton).setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

}
