package com.dzakwan.notesapp.ui.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.dzakwan.notesapp.R
import com.dzakwan.notesapp.data.entity.Notes
import com.dzakwan.notesapp.data.entity.Priority
import com.dzakwan.notesapp.databinding.FragmentAddBinding
import com.dzakwan.notesapp.ui.NotesViewModel
import com.dzakwan.notesapp.ui.ViewModelFactory
import com.dzakwan.notesapp.utils.BindingAdapters.parsePriorityToInt
import com.dzakwan.notesapp.utils.ExtensionFunction.setActionBar
import com.dzakwan.notesapp.utils.HelperFunctions
import com.dzakwan.notesapp.utils.HelperFunctions.parseToPriority
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding as FragmentAddBinding

    private var _addViewModel: NotesViewModel? = null
    private val addViewModel get() = _addViewModel as NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        _addViewModel = activity?.let { obtainViewModel(it) }

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbarAdd.setActionBar(requireActivity())

        binding.spinnerPriorities.onItemSelectedListener =
            HelperFunctions.spinnerListener(requireContext(), binding.priorityIndicator)
    }

    private fun obtainViewModel(activity: FragmentActivity): NotesViewModel? {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NotesViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)

        val action = menu.findItem(R.id.menu_save)
        action.actionView.findViewById<AppCompatImageButton>(R.id.btn_save).setOnClickListener {
            insertNotes()
        }
    }

    private fun insertNotes() {
        binding.apply {
            val title = edtTitle.text.toString()
            val priority = spinnerPriorities.selectedItem.toString()
            val descriptionCompat = edtDescription.text.toString()

            val calendar = Calendar.getInstance().time
            val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format((calendar))

            val notes = Notes(
                0,
                title,
                parseToPriority(priority, context),
                descriptionCompat,
                date
            )
            if (edtTitle.text.isEmpty() || edtDescription.text.isEmpty()) {
                edtTitle.error = "please fill field"
                edtDescription.error = "please fill field"
                Toast.makeText(context, "Please fill fields", Toast.LENGTH_LONG).show()
            }else {
                addViewModel.insertData(notes)
                findNavController().navigate(R.id.action_addFragment_to_homeFragment)
                Toast.makeText(context, "Successfully add note", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // mengubah dari yang awalnya array ke priority


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    // Untuk menggunakan btn di action bar
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId){
//            R.id.menu_save -> findNavController().navigate(R.id.action_addFragment_to_homeFragment)
//        }
//        return super.onOptionsItemSelected(item)
//    }
}