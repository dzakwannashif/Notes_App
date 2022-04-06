package com.dzakwan.notesapp.ui.home

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dzakwan.notesapp.ui.MainActivity
import com.dzakwan.notesapp.R
import com.dzakwan.notesapp.data.entity.Notes
import com.dzakwan.notesapp.databinding.FragmentHomeBinding
import com.dzakwan.notesapp.ui.NotesViewModel
import com.dzakwan.notesapp.utils.ExtensionFunction.setActionBar
import com.dzakwan.notesapp.utils.HelperFunctions
import com.dzakwan.notesapp.utils.HelperFunctions.checkIfDataEmpty
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val homeViewModel by viewModels<NotesViewModel>()

    private val homeAdapter by lazy { HomeAdapter() }

    private var _currentData: List<Notes>? = null
    private val currentData get() = _currentData as List<Notes>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //untuk memberitahu fragment kalau dia punya fragment sendiri
        setHasOptionsMenu(true)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.apply {

            mHelperFunctions = HelperFunctions

            toolbarHome.setActionBar(requireActivity())

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addFragment)
            }

//            toDetail.setOnClickListener {
//                findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
//            }
        }

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvHome.apply {
            homeViewModel.getAllData().observe(viewLifecycleOwner){
                checkIfDataEmpty(it)
                showEmptyDataLayout(it)
                homeAdapter.setData(it)
                _currentData = it
            }
            adapter = homeAdapter
            // StraggedGridLayoutManager itu untuk mengisi yang kosong di rv
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

            swipeToDelete(this)
        }

    }

    private fun showEmptyDataLayout(data: List<Notes>) {
        when(data.isEmpty()) {
            true -> {
                binding.rvHome.visibility = View.INVISIBLE
                binding.imgNoData.visibility = View.VISIBLE
            }
            else -> {
                binding.rvHome.visibility = View.VISIBLE
                binding.imgNoData.visibility = View.INVISIBLE
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)

        // mengaktifkan fungsi search
        val search = menu.findItem(R.id.menu_search)
        val searchAction = search.actionView as? SearchView
        searchAction?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_priority_high -> homeViewModel.sortByHighPriority().observe(this){ dataHigh ->
                homeAdapter.setData(dataHigh)
            }
            R.id.menu_priority_low -> homeViewModel.sortByLowPriority().observe(this){
                homeAdapter.setData(it)
            }
            R.id.menu_delete_all -> confirmDeleteAll()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll() {
        if (currentData.isEmpty()){
            Toast.makeText(requireContext(),"Must create some Text First", Toast.LENGTH_LONG).show()
        } else{
            AlertDialog.Builder(requireContext())
                .setTitle("Delete All Your Notes?")
                .setMessage("Are you sure want clear all of this data?")
                .setPositiveButton("Yes"){_, _->
                    homeViewModel.deleteAllData()
                    Toast.makeText(requireContext(), "Successfully deleted all data", Toast.LENGTH_LONG)
                        .show()
                }
                .setNegativeButton("No"){_,_ ->
                    Toast.makeText(requireContext(), "You has been canceled to delete all data", Toast.LENGTH_LONG)
                        .show()
                }
                .show()
        }
    }

    //mengatur fungsi search ketika tombol search dipencet
    override fun onQueryTextSubmit(query: String?): Boolean {
        val querySearch = "%$query"
        query?.let {
            homeViewModel.searchByQuery(querySearch).observe(this) {
                homeAdapter.setData(it)
            }
        }
        return true
    }

    //mengatur fungsi search ketika sedang di tulis
    override fun onQueryTextChange(newText: String?): Boolean {
        val querySearch = "%$newText%"
        newText?.let {
            homeViewModel.searchByQuery(querySearch).observe(this) {
            homeAdapter.setData(it)
            }
        }
        return true
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = homeAdapter.listNotes[viewHolder.adapterPosition]
                homeViewModel.deleteNote(deletedItem)
                Toast.makeText(context, "Successfully deleted note", Toast.LENGTH_LONG).show()
                restoreData(viewHolder.itemView, deletedItem)
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun restoreData(view: View, deletedItem: Notes) {
        val snackBar = Snackbar.make(
            view, "Deleted: '${deletedItem.title}'", Snackbar.LENGTH_LONG
        )
        snackBar.setTextColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.setAction("Undo") {
            homeViewModel.insertData(deletedItem)
        }
        snackBar.setActionTextColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}