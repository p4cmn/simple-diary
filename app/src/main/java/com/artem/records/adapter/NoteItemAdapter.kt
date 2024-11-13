package com.artem.records.adapter

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.artem.records.databinding.NoteItemBinding
import com.artem.records.model.entity.Note
import com.artem.records.utils.NoteDiffItemCallback
import com.artem.records.R

class NoteItemAdapter (
    private val onEditClickListener: (noteId: Long) -> Unit,
    private val onDeleteClickListener: (note: Note) -> Unit,
): ListAdapter<Note, NoteItemAdapter.NoteItemViewHolder>(NoteDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteItemViewHolder = NoteItemViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onEditClickListener, onDeleteClickListener)
    }

    class NoteItemViewHolder(
        private val binding: NoteItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun inflateFrom(parent: ViewGroup): NoteItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteItemBinding.inflate(layoutInflater, parent, false)
                return NoteItemViewHolder(binding)
            }
        }

        fun bind(
            item: Note,
            onEditClickListener: (noteId: Long) -> Unit,
            onDeleteClickListener: (note: Note) -> Unit,
        ) {
            binding.note = item

            binding.menuButton.setOnClickListener { view ->
                showPopupMenu(view, item, onEditClickListener, onDeleteClickListener)
            }

            binding.root.setOnLongClickListener { view ->
                showPopupMenu(view, item, onEditClickListener, onDeleteClickListener)
                true
            }
        }

        private fun showPopupMenu(
            view: View,
            note: Note,
            onEditClickListener: (noteId: Long) -> Unit,
            onDeleteClickListener: (note: Note) -> Unit
        ) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.note_item_menu)
            styleMenuItem(view, popupMenu, R.id.edit_note, R.color.accent_color_blue)
            styleMenuItem(view, popupMenu, R.id.delete_note, R.color.accent_color_red)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit_note -> {
                        onEditClickListener(note.noteId)
                        true
                    }
                    R.id.delete_note -> {
                        onDeleteClickListener(note)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

        private fun styleMenuItem(view: View, popupMenu: PopupMenu, itemId: Int, colorId: Int) {
            popupMenu.menu.findItem(itemId)?.apply {
                title = SpannableString(title).apply {
                    setSpan(ForegroundColorSpan(
                        ContextCompat.getColor(view.context, colorId)),
                        0,
                        length,
                        0
                    )
                    setSpan(RelativeSizeSpan(1.2f), 0, length, 0)
                }
            }
        }

    }

}
