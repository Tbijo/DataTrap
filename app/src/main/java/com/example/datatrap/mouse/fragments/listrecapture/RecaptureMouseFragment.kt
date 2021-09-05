package com.example.datatrap.mouse.fragments.listrecapture

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentRecaptureMouseBinding
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.Protocol
import com.example.datatrap.models.Specie
import com.example.datatrap.mouse.fragments.UpdateMouseFragmentDirections
import com.example.datatrap.viewmodels.MouseViewModel
import com.example.datatrap.viewmodels.ProtocolViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import com.example.datatrap.viewmodels.SpecieViewModel

class RecaptureMouseFragment : Fragment() {

    private var _binding: FragmentRecaptureMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RecaptureMouseFragmentArgs>()
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var protocolViewModel: ProtocolViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var listSpecie: List<Specie>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var mapSpecie: MutableMap<String, Long>
    private lateinit var mapProtocol: MutableMap<String?, Long>

    private var sex: String? = null
    private var imgName: String? = null
    private var age: String? = null
    private var captureID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentRecaptureMouseBinding.inflate(inflater, container, false)
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

        initMouse()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun initMouse(){
        binding.cbGravit.isChecked = args.mouse.gravidity == 1
        binding.cbLactating.isChecked = args.mouse.lactating == 1
        binding.cbSexActive.isChecked = args.mouse.sexActive == 1
        binding.cbMc.isChecked = args.mouse.MC == 1

        binding.etWeight.setText(args.mouse.weight.toString())
        binding.etBody.setText(args.mouse.body.toString())
        binding.etTail.setText(args.mouse.tail.toString())
        binding.etFeet.setText(args.mouse.feet.toString())
        binding.etTrapId.setText(args.mouse.trapID.toString())
        binding.etEar.setText(args.mouse.ear.toString())
        binding.etTestesLength.setText(args.mouse.testesLength.toString())
        binding.etTestesWidth.setText(args.mouse.testesWidth.toString())
        binding.etEmbryoRight.setText(args.mouse.embryoRight.toString())
        binding.etEmbryoLeft.setText(args.mouse.embryoLeft.toString())
        binding.etEmbryoDiameter.setText(args.mouse.embryoDiameter.toString())
        binding.etMouseNote.setText(args.mouse.note)
        binding.etMcRight.setText(args.mouse.MCright.toString())
        binding.etMcLeft.setText(args.mouse.MCleft.toString())

        when(args.mouse.sex){
            "Male" -> binding.rbMale.isChecked = true
            "Female" -> binding.rbFemale.isChecked = true
            null -> binding.rbNullSex.isChecked = true
        }

        when(args.mouse.age){
            "Adult" -> binding.rbAdult.isChecked = true
            "Subadult" -> binding.rbSubadult.isChecked = true
            "Juvenile" -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when(args.mouse.captureID){
            "Captured" -> binding.rbCaptured.isChecked = true
            "Died" -> binding.rbDied.isChecked = true
            "Escaped" -> binding.rbEscaped.isChecked = true
            "Released" -> binding.rbReleased.isChecked = true
            null -> binding.rbNullCapture.isChecked = true
        }
    }

    private fun initAutoComp(){
        mapSpecie.forEach {
            if (it.value == args.mouse.speciesID){
                binding.autoCompTvSpecie.setText(it.key, false)
            }
        }
        mapProtocol.forEach {
            if (it.value == args.mouse.protocolID){
                binding.autoCompTvProtocol.setText(it.key, false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val dropDownArrSpecie = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapSpecie.keys.toList())
        binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

        val dropDownArrProtocol = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapProtocol.keys.toList())
        binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)

        initAutoComp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.mouse_menu, menu)
        menu.findItem(R.id.menu_rat).isVisible = false
        menu.findItem(R.id.menu_delete).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> recaptureMouse()
            R.id.menu_camera -> goToCamera()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToCamera() {
        val action = UpdateMouseFragmentDirections.actionUpdateMouseFragmentToTakePhotoFragment("Mouse", imgName)
        findNavController().navigate(action)
    }

    private fun recaptureMouse() {
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

        if (checkInput(speciesID, trapID)){
            val mouse = Mouse(0, args.mouse.code, speciesID, protocolID, args.occasion.occasionId,
                args.occasion.localityId, trapID, args.mouse.date, args.mouse.catchTime, sex, age, gravitidy, lactating, sexActive,
                weight, recapture = 1, captureID, body, tail, feet, ear, testesLength, testesWidth, embryoRight, embryoLeft,
                embryoDiameter, MC, MCright, MCleft, note, imgName)

            mouseViewModel.insertMouse(mouse)

            Toast.makeText(requireContext(), "Mouse recaptured.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkInput(specieID: Long, trapID: Int): Boolean {
        return specieID.toString().isNotEmpty() && trapID.toString().isNotEmpty()
    }

}