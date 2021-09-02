package com.example.datatrap.mouse.fragments

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
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

    private var sex: String? = null
    private var imgName: String? = null
    private var age: String? = null
    private var captureID: String? = null
    private var code: Int? = null

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
            mapProtocol[it.protocolName] = it.protocolId
        }

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

        mouseViewModel.countMiceForLocality(args.occasion.localityId).observe(viewLifecycleOwner, Observer {
            code = it
        })

        binding.rgSex.setOnCheckedChangeListener{ radioGroup, checkedId ->
            sex = when(checkedId){
                R.id.rb_male -> "Male"
                R.id.rb_female -> "Female"
                R.id.rb_null_sex -> null
                else -> null
            }
        }

        binding.rgAge.setOnCheckedChangeListener { radioGroup, checkedId ->
            age = when(checkedId){
                R.id.rb_adult -> "Adult"
                R.id.rb_juvenile -> "Juvenile"
                R.id.rb_subadult -> "Subadult"
                R.id.rb_null_age -> null
                else -> null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { radioGroup, checkedId ->
            captureID = when(checkedId){
                R.id.rb_captured -> "Captured"
                R.id.rb_died -> "Died"
                R.id.rb_escaped -> "Escaped"
                R.id.rb_released -> "Released"
                R.id.rb_null_capture -> null
                else -> null
            }
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val dropDownArrSpecie = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapSpecie.keys.toList())
        binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

        val dropDownArrProtocol = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapProtocol.keys.toList())
        binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_menu, menu)
        menu.findItem(R.id.menu_delete).isVisible = false
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
        val speciesID: Long = mapSpecie.getValue(binding.autoCompTvSpecie.text.toString())
        val protocolID: Long? = mapProtocol.getValue(binding.autoCompTvProtocol.text.toString())
        val gravitidy: Int? = if (binding.cbGravit.isChecked) 1 else 0
        val lactating: Int? = if (binding.cbLactating.isChecked) 1 else 0
        val sexActive: Int? = if (binding.cbSexActive.isChecked) 1 else 0
        val trapID: Int = Integer.parseInt(binding.etTrapId.text.toString())
        val weight: Float? = Integer.parseInt(binding.etWeight.text.toString()).toFloat()
        val body: Float? = Integer.parseInt(binding.etBody.text.toString()).toFloat()
        val tail: Float? = Integer.parseInt(binding.etTail.text.toString()).toFloat()
        val feet: Float? = Integer.parseInt(binding.etFeet.text.toString()).toFloat()
        val ear: Float? = Integer.parseInt(binding.etEar.text.toString()).toFloat()
        val testesLength: Float? = Integer.parseInt(binding.etTestesLength.text.toString()).toFloat()
        val testesWidth: Float? = Integer.parseInt(binding.etTestesWidth.text.toString()).toFloat()
        //počet embryí v oboch rohoch maternice a ich priemer
        val embryoRight: Int? = Integer.parseInt(binding.etEmbryoRight.text.toString())
        val embryoLeft: Int? = Integer.parseInt(binding.etEmbryoLeft.text.toString())
        val embryoDiameter: Float? = Integer.parseInt(binding.etEmbryoDiameter.text.toString()).toFloat()
        val MC: Int? = if (binding.cbMc.isChecked) 1 else 0
        //počet placentálnych polypov
        val MCright: Int? = Integer.parseInt(binding.etMcRight.text.toString())
        val MCleft: Int? = Integer.parseInt(binding.etMcLeft.text.toString())
        val note: String? = binding.etMouseNote.text.toString()

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

    private fun checkInput(code: Int?, specieID: Long, trapID: Int): Boolean {
        return code != null && specieID.toString().isNotEmpty() && trapID.toString().isNotEmpty()
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