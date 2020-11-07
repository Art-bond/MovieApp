package ru.d3st.academyandroid.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val startTime: String,
    val name: String
)
