package com.dzakwan.notesapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dzakwan.notesapp.ui.MainActivity
import com.dzakwan.notesapp.R
import com.dzakwan.notesapp.data.entity.Notes
import com.dzakwan.notesapp.databinding.FragmentHomeBinding
import com.dzakwan.notesapp.ui.NotesViewModel
import com.dzakwan.notesapp.utils.ExtensionFunction.setActionBar

class HomeFragment : Fragment() {

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
                checkIsDataEmpty(it)
                homeAdapter.setData(it)
                _currentData = it
            }
            adapter = homeAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            // StraggedGridLayoutManager itu untuk mengisi yang kosong di rv
        }

    }

    private fun checkIsDataEmpty(data: List<Notes>) {
        binding.apply {
            if (data.isEmpty()){
                imgNoData.visibility = View.VISIBLE
                rvHome.visibility = View.INVISIBLE
            } else{
                imgNoData.visibility = View.INVISIBLE
                rvHome.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}