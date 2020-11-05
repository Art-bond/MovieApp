package ru.d3st.academyandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.d3st.academyandroid.data.Event

class EventAdapter : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    var data = listOf<Event>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.txt_list_event_name)
        val eventDate: TextView = itemView.findViewById(R.id.txt_list_event_date)

        fun bind(item: Event) {
            eventDate.text = item.startTime
            eventName.text = item.name
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                        .inflate(R.layout.list_item_event, parent, false)

                return ViewHolder(view)
            }
        }
    }
}