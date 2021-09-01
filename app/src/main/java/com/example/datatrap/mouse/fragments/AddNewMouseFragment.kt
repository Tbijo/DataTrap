package com.example.datatrap.mouse.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddNewMouseBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Protocol
import com.example.datatrap.models.Specie
import com.example.datatrap.viewmodels.MouseViewModel
import com.example.datatrap.viewmodels.ProtocolViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import com.example.datatrap.viewmodels.SpecieViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddNewMouseFragment : Fragment() {

    private var _binding: FragmentAddNewMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddNewMouseFragmentArgs>()
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var protocolViewModel: ProtocolViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private val sexList: List<String?> = listOf("Male", "Female", null)
    private val ageList: List<String?> = listOf("Juvenile", "Subadult", "Adult", null)
    private val captureIdList: List<String?> = listOf("Died", "Captured", "Released", "Escaped", null)

    private lateinit var listSpecie: List<Specie>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var mapSpecie: MutableMap<String, Long>
    private lateinit var mapProtocol: MutableMap<String?, Long>

    var imgName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentAddNewMouseBinding.inflate(inflater, container, false)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)

        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        listSpecie = specieViewModel.specieList.value!!
        listSpecie.forEach {
            mapSpecie[it.speciesCode] = it.specieId
        }

        protocolViewModel = ViewModelProvider(this).get(ProtocolViewModel::class.java)
        listProtocol = protocolViewModel.procolList.value!!
        listProtocol.forEach {
            mapProtocol.put(it.protocolName, it.protocolId)
        }

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_menu, menu)
        menu.getItem(R.id.menu_delete).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> insertMouse()
            R.id.menu_camera -> goToCamera()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToCamera() {
        val action = AddNewMouseFragmentDirections.actionAddNewMouseFragmentToTakePhotoFragment("Mouse", null)
        findNavController().navigate(action)
    }

    private fun insertMouse() {
        val code: Int = countMouseByLocality() + 1 //pazure(špecifický kód jedinca)
        val speciesID: Long
        val protocolID: Long?
        val trapID: Int //samy zadavat //TEXT
        val sex: String? //list male female null
        val age: String? //list Juvenile, Subadult a Adult alebo NULL
        val gravitidy: Int? //bool
        val lactating: Int? //bool
        val sexActive: Int? //bool
        val weight: Float?
        val captureID: String? //list Died, Captured & Released, Escaped a NULL
        val body: Float?
        val tail: Float?
        val feet: Float?
        val ear: Float?
        val testesLength: Float?
        val testesWidth: Float?
        //počet embryí v oboch rohoch maternice a ich priemer
        val embryoRight: Int?
        val embryoLeft: Int?
        val embryoDiameter: Float?
        val MC: Int? //bool
        //počet placentálnych polypov
        val MCright: Int?
        val MCleft: Int?
        val note: String?

        if (checkInput(code, speciesID, trapID)){
            val mouse = Mouse(0, code, speciesID, protocolID, args.occasion.occasionId,
                args.occasion.localityId, trapID, getDate(), getTime(), sex, age, gravitidy, lactating, sexActive,
                weight, recapture = 0, captureID, body, tail, feet, ear, testesLength, testesWidth, embryoRight, embryoLeft,
                embryoDiameter, MC, MCright, MCleft, note, imgName)

            mouseViewModel.insertMouse(mouse)

            Toast.makeText(requireContext(), "New mouse added.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(code: Int, specieID: Long, trapID: Int): Boolean {
        return code.toString().isNotEmpty() && specieID.toString().isNotEmpty() && trapID.toString().isNotEmpty()
    }

    private fun getDate(): String{
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getDateInstance()
        return formatter.format(date)
    }

    private fun getTime(): String{
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getTimeInstance()
        return formatter.format(date)
    }

}