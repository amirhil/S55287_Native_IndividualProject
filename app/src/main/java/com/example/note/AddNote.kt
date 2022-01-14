package com.example.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class AddNote : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val btnSave = findViewById<ImageButton>(R.id.btnSave)
        btnSave.setOnClickListener(){
            val getEntiti = findViewById<EditText>(R.id.inEntiti)
            val getTitle = findViewById<EditText>(R.id.inTitle)
            val getDescrip = findViewById<EditText>(R.id.inDesc)

            val status = checkKey(getEntiti.text.toString())
            val entiti = getEntiti.text.toString()
            val title = getTitle.text.toString()
            val descrip = getDescrip.text.toString()

            if(!status){
                val db = openOrCreateDatabase("note", MODE_PRIVATE,null)
                var sql = "INSERT INTO notedata (entiti,title,description) VALUES ('$entiti','$title','$descrip');"
                db.execSQL(sql)
                subToast("New Title $title added!")
                val intent = Intent(this,MainActivity::class.java).apply {
                }
                startActivity(intent)
            }else{
                subToast("Entiti already exists inside Database!")
            }
        }

    }

    private fun checkKey(entiti:String):Boolean{
        val db = openOrCreateDatabase("note", MODE_PRIVATE,null)
        val sql = "SELECT * FROM notedata WHERE entiti = '$entiti'"
        val cursor = db.rawQuery(sql,null)
        var out = false
        if (cursor.count > 0)
            out = true
        return out
    }

    private fun subToast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}