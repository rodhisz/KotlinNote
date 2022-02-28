package com.example.note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.note.room.Note
import kotlinx.android.synthetic.main.adapter_note.view.*

class NoteAdapter(private val notes: ArrayList<Note>, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    //member note adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate( R.layout.adapter_note, parent,false )
        )
    }

    //member note adapter
    override fun getItemCount() = notes.size

    //member note adapter
    //buat nampilin data
    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.itemView.text_title.text = note.title

        //pencet notenya jadi ke detail note
        holder.itemView.text_title.setOnClickListener {
            listener.onClick( note )
        }

        //pencet icon edit
        holder.itemView.icon_edit.setOnClickListener {
            listener.onUpdate( note )
        }

        //pencet icon delete
        holder.itemView.icon_delete.setOnClickListener {
            listener.onDelete( note )
        }
    }

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun setData(list : List<Note>) {
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener{
        fun onClick(note: Note)
        fun onUpdate(note: Note)
        fun onDelete(note: Note)
    }
}