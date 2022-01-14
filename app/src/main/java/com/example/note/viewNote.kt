package com.example.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class viewNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)

        val intent = this.getIntent()
        val entiti = intent.getStringExtra("entiti")

        supportActionBar?.setTitle(entiti)

        val db = openOrCreateDatabase("note", MODE_PRIVATE,null)
        val sql = "SELECT title,description FROM notedata WHERE entiti = '$entiti'"
        val cursor = db.rawQuery(sql,null)
        var getTitle = ""
        var getDesc = ""

        while (cursor.moveToNext()){
            getTitle = cursor.getString(0)
            getDesc = cursor.getString(1)

        }
        cursor.close()

        val title = findViewById<EditText>(R.id.inTitle)
        val descrip = findViewById<EditText>(R.id.inDesc)

        title.setText(getTitle)
        descrip.setText(getDesc)

        val btnDel = findViewById<FloatingActionButton>(R.id.delBtn)
        btnDel.setOnClickListener(){
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    val db = openOrCreateDatabase("note", MODE_PRIVATE,null)
                    val sql = "DELETE FROM notedata WHERE entiti = '$entiti'"
                    db.execSQL(sql)
                    val intent = Intent(this,MainActivity::class.java).apply {
                    }
                    startActivity(intent)
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        var title2 = ""
        var descrip2 = ""

        title.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                title2 = s.toString()
            }
        })
        descrip.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                descrip2 = s.toString()
            }
        })

        val btnEdit = findViewById<FloatingActionButton>(R.id.editBtn)
        btnEdit.setOnClickListener(){

            val db = openOrCreateDatabase("note", MODE_PRIVATE,null)

            if (title2.isNotEmpty() && descrip2.isNotEmpty()){
                val sql = "UPDATE notedata SET title = '$title2',description = '$descrip2' WHERE entiti = '$entiti'"
                db.execSQL(sql)
                subToast("Title and Description note Updated!")

            }
            if (title2.isEmpty() && descrip2.isNotEmpty()){ // update Description
                val sql = "UPDATE notedata SET description = '$descrip2' WHERE entiti = '$entiti'"
                db.execSQL(sql)
                subToast("Description note Updated!")
            }
            if (title2.isNotEmpty() && descrip2.isEmpty()){ //update Title
                val sql = "UPDATE notedata SET title = '$title2' WHERE entiti = '$entiti'"
                db.execSQL(sql)
                subToast("Title note Updated!")
            }
            if(title2.isEmpty() && descrip2.isEmpty()){
                subToast("No data Updated!")
            }
        }
    }

    private fun subToast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}