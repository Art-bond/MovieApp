package ru.d3st.academyandroid

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*
import ru.d3st.academyandroid.data.Event
import java.text.SimpleDateFormat
import java.util.*


class Second : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val name = findViewById<EditText>(R.id.et_name)
        val date = findViewById<EditText>(R.id.et_date)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        date.setOnClickListener {

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                val text = "$dayOfMonth/$month/$year"
                date.setText(text);

            }, year, month, day)
            dpd.show()

        }

        btn_ok.setOnClickListener {
            val intent = Intent()
            val eventOne = Event(date.text.toString(),name.text.toString())
            Log.i("SecondActivity", "event содержит $eventOne")
            intent.putExtra(EVENT_INTENT, eventOne)
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    companion object {
        const val EVENT_INTENT="event"
    }

}