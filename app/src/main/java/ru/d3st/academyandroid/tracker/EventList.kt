package ru.d3st.academyandroid.tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import ru.d3st.academyandroid.databinding.EventListFragmentBinding
import ru.d3st.academyandroid.storage.EventDataBase
import kotlinx.android.synthetic.main.event_list_fragment.*


class EventList : Fragment() {

    private lateinit var viewModel: EventListViewModel

    private val adapter = EventListAdapter(
        onEdit = { event ->
            findNavController().navigate(
                EventListDirections.actionEventListToDialog(event.id)
            )
        },
        onDelete = { event ->
            NotificationManagerCompat.from(requireContext()).cancel(event.id.toInt())
            viewModel.delete(event)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = EventListFragmentBinding.bind(view)
        val eventDao = EventDataBase.getDatabase(requireContext()).eventDao()
        viewModel = ViewModelProvider(this, ViewModelFactory(eventDao))
            .get(EventListViewModel::class.java)

        viewModel.events.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        event_list.adapter = adapter





        binding.fab.setOnClickListener { fabView ->
            fabView.findNavController().navigate(
                EventListDirections.actionEventListToDialog()
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return EventListFragmentBinding.inflate(inflater, container, false).root
    }
}
