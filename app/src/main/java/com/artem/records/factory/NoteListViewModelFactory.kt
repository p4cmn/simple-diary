package com.artem.records.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artem.records.model.repository.NoteRepository
import com.artem.records.viewmodel.NoteListViewModel

@Suppress("UNCHECKED_CAST")
class NoteListViewModelFactory(
    private val repository: NoteRepository,
    private val userId: Long
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            return NoteListViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
