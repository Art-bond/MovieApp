package ru.d3st.academyandroid

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.util.Log.INFO
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ru.d3st.academyandroid.data.Event
import ru.d3st.academyandroid.data.EventAdapter

class MainActivity : AppCompatActivity() {

    private val eventList = mutableListOf<Event>()
    private val adapter = EventAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, Second::class.java).apply {
            putExtra("media_id", "a1b2c3")
            // ...
        }
        val btn = findViewById<Button>(R.id.btn_to_second)

        btn.setOnClickListener {
            startActivityForResult(intent, CODE_INTENT)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.event_list)
        recyclerView.adapter = adapter


    }

    companion object {
        const val CODE_INTENT = 69
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //if (data == null) return
        //Log.i("event","Event ${event?.name} на ${event?.startTime}")
        if (data != null) {
            val event = data.getParcelableExtra(Second.EVENT_INTENT) as Event?
            Log.i("event", "Event ${event?.name} на ${event?.startTime}")
            eventList.add(event!!)
            Log.i("events", eventList.toString())
            adapter.data = eventList

        }



        super.onActivityResult(requestCode, resultCode, data)

    }
}