package com.artem.records.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem.records.model.entity.Note
import com.artem.records.model.repository.NoteRepository
import com.artem.records.utils.TimeUtils
import kotlinx.coroutines.launch

class NoteEditViewModel(
    private val noteRepository: NoteRepository,
    private val userId: Long,
    private val noteId: Long = -1L
): ViewModel() {

    private val _note = MutableLiveData<Note>().apply {
        value = Note(
            userId = userId,
            title = "",
            content = "",
            timestamp = TimeUtils.getCurrentTimestamp()
        )
    }
    val note: LiveData<Note> get() = _note

    private val _navigateToNoteList = MutableLiveData<Boolean>()
    val navigateToNoteList: LiveData<Boolean> get() = _navigateToNoteList

    init {
        if (noteId != -1L) {
            viewModelScope.launch {
                val existingNote = noteRepository.getNoteById(noteId)
                existingNote?.let {
                    _note.value = it
                }
            }
        }
    }

    fun onSaveButtonClicked() {
        _note.value?.let { currentNote ->
            viewModelScope.launch {
                if (noteId == -1L) {
                    noteRepository.insertNote(currentNote)
                } else {
                    noteRepository.updateNote(currentNote)
                }
                _navigateToNoteList.value = true
            }
        }
    }

    fun navigatedToNoteList() {
        _navigateToNoteList.value = false
    }

}
