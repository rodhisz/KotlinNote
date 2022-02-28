package com.example.note

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.note.room.Constant
import com.example.note.room.Note
import com.example.note.room.NoteDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this) }
    lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListener()
        setupRecyclerView()
    }

    //buat munculin data di logcat
    override fun onStart() {
        super.onStart()
        loadNote()

    }

    //ngeload ulang data
    fun loadNote(){
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNotes()
            Log.d("MainActivity","dbResponse : $notes")

            //setelah nambahin setup recyclerview
            withContext(Dispatchers.Main) {
                noteAdapter.setData( notes )
            }
        }
    }

    //tombol create pindah ke halaman edit
    fun setupListener() {
        button_create.setOnClickListener {
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(noteId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentType)
        )
    }

    //setup recyclerview
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(arrayListOf(), object : NoteAdapter.OnAdapterListener{

            //member object dari adapter,
            // buat munculin detail pas diklik
            override fun onClick(note: Note) {
                //--toast buat percobaan--//
                //Toast.makeText(applicationContext, note.title, Toast.LENGTH_SHORT).show()

                intentEdit(note.id, Constant.TYPE_READ)
            }

            //munculin edit pas diklik
            override fun onUpdate(note: Note) {
                intentEdit(note.id, Constant.TYPE_UPDATE)
            }

            //delete
            override fun onDelete(note: Note) {
                deleteDialog( note )
            }

        })
        list_note.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }

    private fun deleteDialog( note: Note ) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Hapus Note")
            setMessage("Yakin nih mau hapus ${note.title}?")

            //batal
            setNegativeButton("Ga jadi") { dialogInterface, i ->
                dialogInterface.dismiss()
            }

            //yakin
            setPositiveButton("Yakin Sob") { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote( note )
                    loadNote()
                }
            }
        }
        alertDialog.show()
    }
}