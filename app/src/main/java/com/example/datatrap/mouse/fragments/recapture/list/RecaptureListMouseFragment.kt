package com.example.datatrap.mouse.fragments.recapture.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentRecaptureListMouseBinding
import com.example.datatrap.models.tuples.MouseRecapList
import com.example.datatrap.viewmodels.MouseViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RecaptureListMouseFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentRecaptureListMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RecaptureListMouseFragmentArgs>()
    private val mouseViewModel: MouseViewModel by viewModels()
    private lateinit var adapter: RecaptureMouseRecyclerAdapter
    private var mouseList: List<MouseRecapList> = emptyList()

    private val MILLIS_IN_SECOND = 1000L
    private val SECONDS_IN_MINUTE = 60
    private val MINUTES_IN_HOUR = 60
    private val HOURS_IN_DAY = 24
    private val DAYS_IN_YEAR = 365
    private val MILLISECONDS_IN_2_YEAR: Long =
        2 * MILLIS_IN_SECOND * SECONDS_IN_MINUTE * MINUTES_IN_HOUR * HOURS_IN_DAY * DAYS_IN_YEAR

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentRecaptureListMouseBinding.inflate(inflater, container, false)

        adapter = RecaptureMouseRecyclerAdapter()
        val recyclerView = binding.recaptureRecyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            LinearLayoutManager.VERTICAL
        )
        recyclerView.addItemDecoration(dividerItemDecoration)

        adapter.setOnItemClickListener(object: RecaptureMouseRecyclerAdapter.MyClickListener {
            override fun useClickListener(position: Int) {
                goToRecaptureMouse(position)
            }
            override fun useLongClickListener(position: Int) {
                Toast.makeText(requireContext(), "You have found an easter egg. :D", Toast.LENGTH_SHORT).show()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_list_menu, menu)
        menu.findItem(R.id.menu_recapture).isVisible = false
        menu.findItem(R.id.menu_occ_info).isVisible = false
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (!newText.isNullOrBlank() && newText.isDigitsOnly()) {
            searchMice(Integer.parseInt(newText))
        }
        return true
    }

    private fun searchMice(code: Int) {
        mouseViewModel.getMiceForRecapture(code, Calendar.getInstance().time.time, MILLISECONDS_IN_2_YEAR).observe(viewLifecycleOwner) { mice ->
            mice.let {
                adapter.setData(it)
                mouseList = it
            }
        }
    }

    private fun goToRecaptureMouse(position: Int) {
        val mouse = mouseList[position]
        val action = RecaptureListMouseFragmentDirections.actionRecaptureListMouseFragmentToRecaptureMouseFragment(mouse, args.occList)
        findNavController().navigate(action)
    }

}