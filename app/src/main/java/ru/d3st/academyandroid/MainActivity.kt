package ru.d3st.academyandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.d3st.academyandroid.notification.Notifier
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Notifier.init(this)

        }
    }
