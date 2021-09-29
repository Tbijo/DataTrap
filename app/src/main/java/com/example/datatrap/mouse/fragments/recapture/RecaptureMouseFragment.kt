package com.example.datatrap.mouse.fragments.recapture

import android.app.AlertDialog
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
import com.example.datatrap.myenums.CaptureID
import com.example.datatrap.myenums.MouseAge
import com.example.datatrap.myenums.TrapID
import com.example.datatrap.viewmodels.MouseViewModel
import com.example.datatrap.viewmodels.ProtocolViewModel
import com.example.datatrap.viewmodels.SharedViewModel
import com.example.datatrap.viewmodels.SpecieViewModel
import java.util.*

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
    private var isMale: Boolean = false

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

        setListeners()

        initMouseValuesToView()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val dropDownArrSpecie = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapSpecie.keys.toList())
        binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

        val dropDownArrProtocol = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapProtocol.keys.toList())
        binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)

        val dropDownArrTrapID = ArrayAdapter(requireContext(), R.layout.dropdown_names, TrapID.values())
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

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

        binding.autoCompTvTrapId.setText(args.mouse.trapID)
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

    private fun initMouseValuesToView(){
        binding.cbGravit.isChecked = args.mouse.gravidity == 1
        binding.cbLactating.isChecked = args.mouse.lactating == 1
        binding.cbSexActive.isChecked = args.mouse.sexActive == 1
        binding.cbMc.isChecked = args.mouse.MC == 1

        binding.etWeight.setText(args.mouse.weight.toString())
        binding.etBody.setText(args.mouse.body.toString())
        binding.etTail.setText(args.mouse.tail.toString())
        binding.etFeet.setText(args.mouse.feet.toString())
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
            "Male" -> {
                binding.rbMale.isChecked = true
                isMale = true
                showNonMaleFields()
            }
            "Female" -> {
                binding.rbFemale.isChecked = true
                isMale = false
                hideNonMaleFields()
            }
            null -> binding.rbNullSex.isChecked = true
        }

        when(args.mouse.age){
            MouseAge.Adult.name -> binding.rbAdult.isChecked = true
            MouseAge.Subadult.name -> binding.rbSubadult.isChecked = true
            MouseAge.Juvenile.name -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when(args.mouse.captureID){
            CaptureID.Captured.name -> binding.rbCaptured.isChecked = true
            CaptureID.Died.name -> binding.rbDied.isChecked = true
            CaptureID.Escaped.name -> binding.rbEscaped.isChecked = true
            CaptureID.Released.name -> binding.rbReleased.isChecked = true
            null -> binding.rbNullCapture.isChecked = true
        }
    }

    private fun setListeners(){
        binding.rgSex.setOnCheckedChangeListener{ radioGroup, checkedId ->
            when(checkedId){
                R.id.rb_male -> {
                    sex = "Male"
                    showNonMaleFields()
                    isMale = true
                }
                R.id.rb_female -> {
                    sex =  "Female"
                    hideNonMaleFields()
                    isMale = false
                }
                R.id.rb_null_sex -> {
                    sex = null
                    isMale = false
                    showNonMaleFields()
                }
            }
        }

        binding.rgAge.setOnCheckedChangeListener { radioGroup, checkedId ->
            age = when(checkedId){
                R.id.rb_adult -> MouseAge.Adult.name
                R.id.rb_juvenile -> MouseAge.Juvenile.name
                R.id.rb_subadult -> MouseAge.Subadult.name
                R.id.rb_null_age -> null
                else -> null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { radioGroup, checkedId ->
            captureID = when(checkedId){
                R.id.rb_captured -> CaptureID.Captured.name
                R.id.rb_died -> CaptureID.Died.name
                R.id.rb_escaped -> CaptureID.Escaped.name
                R.id.rb_released -> CaptureID.Released.name
                R.id.rb_null_capture -> null
                else -> null
            }
        }
    }

    private fun hideNonMaleFields(){
        binding.etEmbryoRight.visibility = View.INVISIBLE
        binding.etEmbryoLeft.visibility = View.INVISIBLE
        binding.etEmbryoDiameter.visibility = View.INVISIBLE
        binding.cbMc.visibility = View.INVISIBLE
        binding.etMcRight.visibility = View.INVISIBLE
        binding.etMcLeft.visibility = View.INVISIBLE
    }

    private fun showNonMaleFields(){
        binding.etEmbryoRight.visibility = View.VISIBLE
        binding.etEmbryoLeft.visibility = View.VISIBLE
        binding.etEmbryoDiameter.visibility = View.VISIBLE
        binding.cbMc.visibility = View.VISIBLE
        binding.etMcRight.visibility = View.VISIBLE
        binding.etMcLeft.visibility = View.VISIBLE
    }

    private fun goToCamera() {
        val action = UpdateMouseFragmentDirections.actionUpdateMouseFragmentToTakePhotoFragment("Mouse", imgName)
        findNavController().navigate(action)
    }

    private fun recaptureMouse() {
        val speciesID: Long = mapSpecie.getValue(binding.autoCompTvSpecie.text.toString())
        val trapID: String = binding.autoCompTvTrapId.text.toString()

        if (checkInput(speciesID, trapID)){
            // zmenit pohlavie vsetkym predch. mysiam ak sa zmenilo
            changeSexIfNecesary()

            // recapture mouse
            val mouse: Mouse = args.mouse
            mouse.mouseId = 0
            mouse.primeMouseID = args.mouse.mouseId
            mouse.speciesID = speciesID
            mouse.protocolID = mapProtocol.getValue(binding.autoCompTvProtocol.text.toString())
            mouse.occasionID = args.occasion.occasionId
            mouse.localityID = args.occasion.localityID
            mouse.trapID = trapID
            mouse.catchDateTime = Calendar.getInstance().time
            mouse.sex = sex
            mouse.age = age
            mouse.gravidity = if (binding.cbGravit.isChecked) 1 else 0
            mouse.lactating = if (binding.cbLactating.isChecked) 1 else 0
            mouse.sexActive = if (binding.cbSexActive.isChecked) 1 else 0
            mouse.weight = Integer.parseInt(binding.etWeight.text.toString()).toFloat()
            mouse.recapture = 1
            mouse.captureID = captureID
            mouse.body = Integer.parseInt(binding.etBody.text.toString()).toFloat()
            mouse.tail = Integer.parseInt(binding.etTail.text.toString()).toFloat()
            mouse.feet = Integer.parseInt(binding.etFeet.text.toString()).toFloat()
            mouse.ear = Integer.parseInt(binding.etEar.text.toString()).toFloat()
            mouse.testesLength = Integer.parseInt(binding.etTestesLength.text.toString()).toFloat()
            mouse.testesWidth = Integer.parseInt(binding.etTestesWidth.text.toString()).toFloat()
            //počet embryí v oboch rohoch maternice a ich priemer
            mouse.embryoRight = if (isMale) null else Integer.parseInt(binding.etEmbryoRight.text.toString())
            mouse.embryoLeft = if (isMale) null else Integer.parseInt(binding.etEmbryoLeft.text.toString())
            mouse.embryoDiameter = if (isMale) null else Integer.parseInt(binding.etEmbryoDiameter.text.toString()).toFloat()
            mouse.MC = if (isMale) null else { if (binding.cbMc.isChecked) 1 else 0 }
            //počet placentálnych polypov
            mouse.MCright = if (isMale) null else Integer.parseInt(binding.etMcRight.text.toString())
            mouse.MCleft = if (isMale) null else Integer.parseInt(binding.etMcLeft.text.toString())
            mouse.note = binding.etMouseNote.text.toString()
            mouse.imgName = imgName

            checkWeightAndSave(mouse)
        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse) {
        val specie: Specie = specieViewModel.getSpecie(mouse.speciesID).value!!

        if (mouse.weight!! > specie.maxWeight!! || mouse.weight!! < specie.minWeight!!){
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){_, _ ->
                mouseViewModel.insertMouse(mouse)

                Toast.makeText(requireContext(), "Mouse recaptured.", Toast.LENGTH_SHORT).show()

                findNavController().navigateUp()
            }
                .setNegativeButton("No"){_, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        }else{
            mouseViewModel.insertMouse(mouse)

            Toast.makeText(requireContext(), "Mouse recaptured.", Toast.LENGTH_SHORT).show()

            findNavController().navigateUp()
        }
    }

    private fun checkInput(specieID: Long, trapID: String): Boolean {
        return specieID.toString().isNotEmpty() && trapID.isNotEmpty()
    }

    private fun changeSexIfNecesary(){
        if (args.mouse.sex != sex){
            val updateMouseList: List<Mouse> = mouseViewModel.getMiceForCode(args.mouse.code!!).value!!
            updateMouseList.forEach {
                it.sex = sex
                mouseViewModel.updateMouse(it)
            }
        }
    }

}