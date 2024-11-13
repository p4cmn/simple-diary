package com.artem.records.utils

import androidx.recyclerview.widget.DiffUtil
import com.artem.records.model.entity.Note

class NoteDiffItemCallback: DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.noteId == newItem.noteId

    override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem == newItem

}
