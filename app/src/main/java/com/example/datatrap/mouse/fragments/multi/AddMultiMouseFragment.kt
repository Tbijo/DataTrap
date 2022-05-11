package com.example.datatrap.mouse.fragments.multi

import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddMultiMouseBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.other.MultiMouse
import com.example.datatrap.models.tuples.SpecList
import com.example.datatrap.myenums.EnumSpecie
import com.example.datatrap.viewmodels.MouseViewModel
import com.example.datatrap.viewmodels.SpecieViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddMultiMouseFragment : Fragment() {

    private var _binding: FragmentAddMultiMouseBinding? = null
    private val binding get() = _binding!!

    private val mouseViewModel: MouseViewModel by viewModels()
    private val specieViewModel: SpecieViewModel by viewModels()

    private lateinit var adapter: MouseMultiRecyclerAdapter

    private val args by navArgs<AddMultiMouseFragmentArgs>()

    private lateinit var listSpecie: List<SpecList>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMultiMouseBinding.inflate(inflater, container, false)

        val list = (1..args.occlist.numTraps).toList()
        adapter = MouseMultiRecyclerAdapter(list, requireContext())
        binding.multiMouseRecycler.adapter = adapter
        binding.multiMouseRecycler.layoutManager = LinearLayoutManager(requireContext())

        val dividerItemDecoration = DividerItemDecoration(
            binding.multiMouseRecycler.context,
            LinearLayoutManager.VERTICAL
        )
        binding.multiMouseRecycler.addItemDecoration(dividerItemDecoration)

        binding.btnAddRow.setOnClickListener {
            adapter.addRow()
        }

        binding.btnRmvRow.setOnClickListener {
            adapter.removeRow()
        }

        val listSpec = EnumSpecie.values().map { it.name }

        specieViewModel.getNonSpecie(listSpec).observe(viewLifecycleOwner) {
            listSpecie = it
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_menu, menu)
        menu.findItem(R.id.menu_delete).isVisible = false
        menu.findItem(R.id.menu_rat).isVisible = false
        menu.findItem(R.id.menu_camera).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> insertMouse()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertMouse() {
        val map = adapter.getData()

        if (!checkInput(map)) {
            return
        }

        val deviceID: String = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        val multiMouse: MutableList<Mouse> = mutableListOf()

        map.forEach {
            val mouse = Mouse(
                0,
                null,
                deviceID,
                null,
                speciesID = getSpecieId(it.value.specieID!!)!!,
                null,
                occasionID = args.occlist.occasionId,
                localityID = args.occlist.localityID,
                trapID = it.value.trapID,
                Calendar.getInstance().time,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )

            multiMouse.add(mouse)
        }

        mouseViewModel.insertMultiMouse(multiMouse)

        Toast.makeText(requireContext(), "Multiple mice added.", Toast.LENGTH_SHORT).show()

        findNavController().navigateUp()
    }

    private fun getSpecieId(specieCode: String): Long? =
        listSpecie.find {
            it.speciesCode == specieCode
        }?.specieId

    private fun checkInput(map: MutableMap<Int, MultiMouse>): Boolean {
        map.forEach {
            if (it.value.specieID == null) {
                Toast.makeText(
                    requireContext(),
                    "SpecieID at position ${it.key} not filled.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
            if (it.value.trapID == null) {
                Toast.makeText(
                    requireContext(),
                    "TrapID at position ${it.key} not filled.",
                    Toast.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }
}