package com.dzakwan.notesapp.ui.detail

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import com.dzakwan.notesapp.R
import com.dzakwan.notesapp.data.entity.Notes
import com.dzakwan.notesapp.databinding.FragmentDetailBinding
import com.dzakwan.notesapp.ui.NotesViewModel
import com.dzakwan.notesapp.utils.ExtensionFunction.setActionBar

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding as FragmentDetailBinding

    private val navArgs by navArgs<DetailFragmentArgs>()

    private val detailViewModel by viewModels<NotesViewModel>()

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

        binding.safeArgs = navArgs

        binding.toolbarDetail.setActionBar(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_edit -> {
                val action = DetailFragmentDirections
                    .actionDetailFragmentToUpdateFragment(
                        navArgs.currentItem
                    )
//                    .actionDetailFragmentToUpdateFragment(
//                    Notes(
//                        navArgs.currentItem.id,
//                        navArgs.currentItem.title,
//                        navArgs.currentItem.priority,
//                        navArgs.currentItem.description,
//                        navArgs.currentItem.date
//                    )
//                )
                findNavController().navigate(action)
            }

            R.id.menu_delete -> confirmDeleteNote()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteNote() {
        context?.let {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete '${navArgs.currentItem.title}'?")
                .setMessage("Are you sure want to remove this '${navArgs.currentItem.title}'?")
                .setPositiveButton("Yes") { _, _ ->
                    detailViewModel.deleteNote(navArgs.currentItem)
                    findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
                    Toast.makeText(it, "Successfully delete '${navArgs.currentItem.title}'", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}