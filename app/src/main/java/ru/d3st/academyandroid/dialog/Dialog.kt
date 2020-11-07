package ru.d3st.academyandroid.dialog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_fragment.view.*
import ru.d3st.academyandroid.databinding.DialogFragmentBinding
import ru.d3st.academyandroid.domain.Event
import ru.d3st.academyandroid.storage.EventDataBase
import ru.d3st.academyandroid.tracker.ViewModelFactory
import java.util.*

class Dialog : BottomSheetDialogFragment() {


    private lateinit var viewModel: DialogViewModel

    private enum class EditingState {
        NEW_EVENT,
        EXISTING_EVENT
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        return DialogFragmentBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val eventDao = EventDataBase.getDatabase(requireContext()).eventDao()

        viewModel = ViewModelProvider(
                this,
                ViewModelFactory(eventDao)).get(DialogViewModel::class.java)

        val binding = DialogFragmentBinding.bind(view)

        //обнуление
        var event: Event? = null
        //создаем контейнер для передачи данных между фрагментами
        val args: DialogArgs by navArgs()
        val editingState =
                if (args.itemId > 0) EditingState.EXISTING_EVENT
                else EditingState.NEW_EVENT

        if (editingState == EditingState.EXISTING_EVENT) {
            // Request to edit an existing item, whose id was passed in as an argument.
            // Retrieve that item and populate the UI with its details
            viewModel.get(args.itemId).observe(viewLifecycleOwner, { item ->
                binding.eventItemName.setText(item.name)
                binding.eventItemDate.setText(item.startTime)
                event = item
            })
        }
        binding.doneButton.setOnClickListener {

            viewModel.addData(
                    event?.id ?: 0,
                    binding.eventItemDate.text.toString(),
                    binding.eventItemName.text.toString()
            )

            dismiss()
        }
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.eventItemDate.setOnClickListener {

            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                val text = "$dayOfMonth/$monthOfYear/$year"
                binding.eventItemDate.setText(text);

            }, year, month, day)
            dpd.show()

        }
    }
}