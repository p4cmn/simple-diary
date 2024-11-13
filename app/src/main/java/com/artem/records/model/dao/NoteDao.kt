package com.artem.records.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.artem.records.model.entity.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note_table WHERE user_id = :userId ORDER BY timestamp DESC")
    fun getNotesByUserId(userId: Long): LiveData<List<Note>>

    @Query("SELECT * FROM note_table WHERE noteId = :noteId")
    suspend fun getNoteById(noteId: Long): Note?

}
