package ru.d3st.academyandroid.tracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.d3st.academyandroid.databinding.EventItemBinding
import ru.d3st.academyandroid.domain.Event

class EventListAdapter(private var onEdit: (Event) -> Unit, private var onDelete: (Event) -> Unit) :
    ListAdapter<Event, EventListAdapter.ViewHolder>(EventDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onEdit,
            onDelete
        )
    }

    class ViewHolder(
        private val binding: EventItemBinding,
        private var onEdit: (Event) -> Unit,
        private var onDelete: (Event) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        var eventId: Long = -1
        private var eventName= binding.eventItemName
        private val eventDate = binding.eventItemDate
        var event: Event? = null

        fun bind(item: Event) {
            eventId = item.id
            eventName.text = item.name
            eventDate.text = item.startTime
            this.event = item
            binding.deleteButton.setOnClickListener {
                onDelete(item)
            }
            binding.root.setOnClickListener {
                onEdit(item)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}