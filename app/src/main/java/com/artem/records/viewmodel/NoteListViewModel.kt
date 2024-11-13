package com.artem.records.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem.records.model.entity.Note
import com.artem.records.model.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val noteRepository: NoteRepository,
    userId: Long
): ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    val notes: LiveData<List<Note>> = noteRepository.getAllNotesByUserId(userId).also {
        _isLoading.value = false
    }

    private val _navigateToEditNote = MutableLiveData<Long?>()
    val navigateToEditNote: LiveData<Long?> = _navigateToEditNote

    private val _navigateToCreateNote = MutableLiveData<Boolean>()
    val navigateToCreateNote: LiveData<Boolean> = _navigateToCreateNote

    fun onEditButtonClicked(noteId: Long) {
        _navigateToEditNote.value = noteId
    }

    fun navigatedToEdit() {
        _navigateToEditNote.value = null
    }

    fun onDeleteButtonClicked(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    fun onAddButtonClicked() {
        _navigateToCreateNote.value = true
    }

    fun navigatedToCreate() {
        _navigateToCreateNote.value = false
    }

}
