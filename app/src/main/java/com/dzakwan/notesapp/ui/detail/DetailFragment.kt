package com.dzakwan.notesapp.ui.detail

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.dzakwan.notesapp.R
import com.dzakwan.notesapp.databinding.FragmentDetailBinding
import com.dzakwan.notesapp.utils.ExtensionFunction.setActionBar

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding as FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbarDetail.setActionBar(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_edit -> findNavController().navigate(R.id.action_detailFragment_to_updateFragment)

            R.id.menu_delete -> confirmDeleteNote()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteNote() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle("Delete Note?")
                .setMessage("Are you sure want to remove this Note?")
                .setPositiveButton("Of Course") { _, _ ->
                    findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
                    Toast.makeText(it, "Successfully delete Note", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { _, _ ->
                    findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
                    Toast.makeText(it, "the delete has been canceled", Toast.LENGTH_SHORT).show()
                }
                .setNeutralButton("Cancel") { _, _ ->
                    Toast.makeText(it, "You has been canceled the progress", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

}