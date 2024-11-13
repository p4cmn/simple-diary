package com.artem.records.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note (

    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L,

    @ColumnInfo(name = "user_id")
    var userId: Long,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "content")
    var content: String = "",

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis()

)
