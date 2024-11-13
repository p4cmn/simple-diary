package com.artem.records.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (

    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0L,

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "password_hash")
    var passwordHash: String = ""

)
