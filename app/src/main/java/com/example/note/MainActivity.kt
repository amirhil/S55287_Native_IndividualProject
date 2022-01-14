package com.example.note

import android.content.Context
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var entities = ArrayList<String>()

        if(!dbExist(this,"note")){
            createDB();
        }

        val db = openOrCreateDatabase("note", MODE_PRIVATE,null)
        val sql = "SELECT entiti FROM notedata"
        val c: Cursor = db.rawQuery(sql,null)
        while (c.moveToNext()){
            val entiti = c.getString(0).toString()
            entities.add(entiti)

        }
        c.close()

        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,entities)
        val lv = findViewById<ListView>(R.id.lv)
        lv.setAdapter(myAdapter)

        lv.onItemClickListener = AdapterView.OnItemClickListener{ adapter, v, position, arg3 ->
            val value = adapter.getItemAtPosition(position).toString()
            val intent = Intent(this,viewNote::class.java).apply {
                putExtra("entiti",value.toString())
            }
            startActivity(intent)

        }

        val fab = findViewById<FloatingActionButton>(R.id.newNote)
        fab.setOnClickListener(){
            val intent = Intent(this,AddNote::class.java).apply {

            }
            startActivity(intent)
        }

    }

    private fun dbExist(c: Context, dbName:String ):Boolean{
        val dbFile: File = c.getDatabasePath(dbName)
        return dbFile.exists()
    }

    private fun createDB(){
        val db = openOrCreateDatabase("note", MODE_PRIVATE,null)
        subToast("Database note created!")
        val sqlText = "CREATE TABLE IF NOT EXISTS notedata " +
                "(entiti VARCHAR(30) PRIMARY KEY, " +
                "title VARCHAR(30) NOT NULL, " +
                "description VARCHAR(300) NOT NULL " +
                ");"
        subToast("Table noteData created!")
        db.execSQL(sqlText)
        var nextSQL = "INSERT INTO notedata (entiti,title,description) VALUES ('1','nota padat','Content Nota padat');"
        db.execSQL(nextSQL)
        nextSQL = "INSERT INTO notedata (entiti,title,description) VALUES ('12','nota padat','Content Nota padat');"
        db.execSQL(nextSQL)
        nextSQL = "INSERT INTO notedata (entiti,title,description) VALUES ('13','nota padat','Content Nota padat');"
        db.execSQL(nextSQL)
        subToast("3 sample entities added!")
    }

    private fun subToast(msg: String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }
}