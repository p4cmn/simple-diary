package com.artem.records.model.repository

import com.artem.records.model.dao.NoteDao
import com.artem.records.model.entity.Note

class NoteRepository private constructor(
    private val noteDao: NoteDao
) {

    suspend fun insertNote(note: Note): Long {
        return noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        return noteDao.update(note)
    }

    suspend fun deleteNote(note: Note) {
        return noteDao.delete(note)
    }

    suspend fun getNoteById(noteId: Long): Note? {
        return noteDao.getNoteById(noteId)
    }

    fun getAllNotesByUserId(userId: Long) = noteDao.getNotesByUserId(userId)

    companion object {
        @Volatile
        private var instance: NoteRepository? = null

        fun getInstance(noteDao: NoteDao): NoteRepository {
            return instance ?: synchronized(this) {
                instance ?: NoteRepository(noteDao).also { instance = it }
            }
        }
    }

}
