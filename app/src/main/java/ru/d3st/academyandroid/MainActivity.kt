package ru.d3st.academyandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, Second::class.java)

        val btn = findViewById<Button>(R.id.btn_to_second)



        btn.setOnClickListener{
            startActivityForResult(intent, Companion.REQUEST_CODE)
        }
    }

    companion object {
        const val REQUEST_CODE = 69
    }
}