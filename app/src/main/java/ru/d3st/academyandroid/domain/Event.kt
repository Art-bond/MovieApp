package ru.d3st.academyandroid.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(
    val eventId: Int,
    val startTime: Long,
    val name: String

) : Parcelable