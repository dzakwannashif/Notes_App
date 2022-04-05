package com.dzakwan.notesapp.ui.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import com.dzakwan.notesapp.R
import com.dzakwan.notesapp.data.entity.Notes
import com.dzakwan.notesapp.databinding.FragmentUpdateBinding
import com.dzakwan.notesapp.ui.NotesViewModel
import com.dzakwan.notesapp.ui.detail.DetailFragment
import com.dzakwan.notesapp.utils.ExtensionFunction.setActionBar
import com.dzakwan.notesapp.utils.HelperFunctions.parseToPriority
import com.dzakwan.notesapp.utils.HelperFunctions.spinnerListener
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {

    private var _binding : FragmentUpdateBinding? = null
    private val binding get() = _binding as FragmentUpdateBinding

    private val safeArgs: UpdateFragmentArgs by navArgs()

    private val updateViewModel by viewModels<NotesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveArgs = safeArgs

        setHasOptionsMenu(true)


        binding.apply {
            toolbarUpdate.setActionBar(requireActivity())
            spinnerPrioritiesUpdate.onItemSelectedListener = spinnerListener(context, binding.priorityIndicator)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_save, menu)
        val action = menu.findItem(R.id.menu_save)
        action.actionView.findViewById<AppCompatImageButton>(R.id.btn_save).setOnClickListener {
            updateNote()
        }
    }

    private fun updateNote() {
        with(binding) {
            val title = edtTitleUpdate.text.toString()
            val priority = spinnerPrioritiesUpdate.selectedItem.toString()
            val descriptionCompat = edtDescriptionUpdate.text.toString()

            val calendar = Calendar.getInstance().time
            val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format((calendar))

            val notes = Notes(
                safeArgs.currentItem.id,
                title,
                parseToPriority(priority, context),
                descriptionCompat,
                date
            )
            if (edtTitleUpdate.text.isEmpty() || edtDescriptionUpdate.text.isEmpty()) {
                edtTitleUpdate.error = "please fill field"
                edtDescriptionUpdate.error = "please fill field"
                Toast.makeText(context, "Please fill fields", Toast.LENGTH_LONG).show()
            }else {
                updateViewModel.insertData(notes)
                val action = UpdateFragmentDirections.actionUpdateFragmentToDetailFragment(notes)
                findNavController().navigate(action)
                Toast.makeText(context, "Successfully add note", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}