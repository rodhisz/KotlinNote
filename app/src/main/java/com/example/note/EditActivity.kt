package com.example.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.note.room.Constant
import com.example.note.room.Note
import com.example.note.room.NoteDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {

    val db by lazy { NoteDB(this) }
    private var noteId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        setupView()
        setupListener()

        //coba nampilin id pake toast
        //noteId = intent.getIntExtra("intent_id",0)
        //Toast.makeText(applicationContext, noteId.toString(), Toast.LENGTH_SHORT).show()
    }

    fun setupView(){

        //buat nampilin tombol back (sama yang bawah juga)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val intentType = intent.getIntExtra("intent_type",0)

        //kasih kondisi
        when(intentType) {
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }

            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getNote()
            }

            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getNote()
            }
        }
    }


    private fun setupListener() {

        //save data pas create
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().addNote(
                    Note(0, edit_title.text.toString(), edit_note.text.toString())
                )
                finish()
            }
        }

        //save data pas update
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().updateNote(
                    Note(noteId, edit_title.text.toString(), edit_note.text.toString())
                )
                finish()
            }
        }
    }

    //nampilin detail note
    fun getNote(){
        noteId = intent.getIntExtra("intent_id",0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNote( noteId )[0]

            edit_title.setText( notes.title )
            edit_note.setText( notes.note )
        }
    }

    //buat nampilin tombol back
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}