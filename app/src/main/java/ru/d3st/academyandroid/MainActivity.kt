package ru.d3st.academyandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.to_movie_detail_button)
        btn.setOnClickListener {
            val intent = Intent(this, MovieDetailsActivity::class.java)

            startActivity(intent)
        }
    }
}