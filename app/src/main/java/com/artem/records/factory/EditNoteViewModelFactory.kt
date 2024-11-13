package com.artem.records.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artem.records.model.repository.NoteRepository
import com.artem.records.viewmodel.NoteEditViewModel

@Suppress("UNCHECKED_CAST")
class EditNoteViewModelFactory(
    private val noteRepository: NoteRepository,
    private val userId: Long,
    private val noteId: Long = -1L
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteEditViewModel::class.java)) {
            return NoteEditViewModel(noteRepository, userId, noteId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
