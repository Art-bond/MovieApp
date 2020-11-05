package ru.d3st.academyandroid.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val startTime: String,
    val name: String
) : Parcelable
