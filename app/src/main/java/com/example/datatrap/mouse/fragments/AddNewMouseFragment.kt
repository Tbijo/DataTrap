package com.example.datatrap.mouse.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddNewMouseBinding
import com.example.datatrap.models.*
import com.example.datatrap.mouse.fragments.generator.CodeGenerator
import com.example.datatrap.myenums.EnumCaptureID
import com.example.datatrap.myenums.EnumMouseAge
import com.example.datatrap.myenums.EnumSex
import com.example.datatrap.myenums.EnumTrapID
import com.example.datatrap.viewmodels.*
import java.util.*

class AddNewMouseFragment : Fragment() {

    private var _binding: FragmentAddNewMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddNewMouseFragmentArgs>()
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var protocolViewModel: ProtocolViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var occasionViewModel: OccasionViewModel
    private lateinit var userViewModel: UserViewModel

    private lateinit var listSpecie: List<Specie>
    private lateinit var activeMouseList: List<Mouse>

    private lateinit var mapSpecie: MutableMap<String, Long>
    private lateinit var mapProtocol: MutableMap<String, Long?>

    private var oldCode: Int = 0
    private var code: Int? = null
    private var sex: String? = null
    private var imgName: String? = null
    private var age: String? = null
    private var captureID: String? = null
    private var speciesID: Long = 0
    private var specie: Specie? = null
    private var gravitidy: Boolean? = null
    private var lactating: Boolean? = null
    private var embryoRight: Int? = null
    private var embryoLeft: Int? = null
    private var embryoDiameter: Float? = null
    private var MC: Boolean? = null
    private var MCright: Int? = null
    private var MCleft: Int? = null
    private var body: Float? = null
    private var tail: Float? = null
    private var feet: Float? = null
    private var ear: Float? = null

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
        _binding = FragmentAddNewMouseBinding.inflate(inflater, container, false)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        protocolViewModel = ViewModelProvider(this).get(ProtocolViewModel::class.java)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

        mapSpecie = mutableMapOf()
        mapProtocol = mutableMapOf()

        fillDropDown()

        mouseViewModel.countMiceForLocality(args.occasion.localityID).observe(viewLifecycleOwner, {
            oldCode = it + 1
        })

        binding.btnGenCode.setOnClickListener {
            generateCode()
        }

        setListeners()
        setListenerToTrapID()

        mouseViewModel.getActiveMiceOfLocality(
            args.occasion.localityID,
            Calendar.getInstance().time.time,
            MILLISECONDS_IN_2_YEAR).observe(viewLifecycleOwner, { mouseList ->
            activeMouseList = mouseList
        })

        // generate code this is to get team
        userViewModel.activeUser.observe(viewLifecycleOwner, { user ->
            if (user != null) {
                val codeGen = CodeGenerator(oldCode, specie?.upperFingers!!, user.team, activeMouseList)
                code = codeGen.generateCode()
                if(code == 0) {
                    Toast.makeText(requireContext(), "No code is available.", Toast.LENGTH_LONG).show()
                }
                binding.etCodeMouseAdd.setText(code.toString())
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        fillDropDown()

        val dropDownArrTrapID = ArrayAdapter(requireContext(), R.layout.dropdown_names, EnumTrapID.myValues())
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)
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
            R.id.menu_rat -> showDrawnRat()
            R.id.menu_camera -> goToCamera()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fillDropDown() {
        specieViewModel.specieList.observe(viewLifecycleOwner, {
            listSpecie = it
            it.forEach { specie ->
                mapSpecie[specie.speciesCode] = specie.specieId
            }
            val dropDownArrSpecie = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapSpecie.keys.toList())
            binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)
        })

        protocolViewModel.procolList.observe(viewLifecycleOwner, {
            it.forEach { protocol ->
                mapProtocol[protocol.protocolName] = protocol.protocolId
            }
            mapProtocol["null"] = null

            val dropDownArrProtocol = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapProtocol.keys.toList())
            binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)
        })
    }

    private fun setListeners(){
        binding.rgSex.setOnCheckedChangeListener{ radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbMale.id -> {
                    sex = EnumSex.MALE.myName
                    hideNonMaleFields()
                }
                binding.rbFemale.id -> {
                    sex = EnumSex.FEMALE.myName
                    showNonMaleFields()
                }
                binding.rbNullSex.id -> {
                    sex = null
                    hideNonMaleFields()
                }
            }
        }

        binding.rgAge.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbAdult.id -> age = EnumMouseAge.ADULT.myName
                binding.rbJuvenile.id -> age = EnumMouseAge.JUVENILE.myName
                binding.rbSubadult.id -> age = EnumMouseAge.SUBADULT.myName
                binding.rbNullAge.id -> age = null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbCaptured.id -> captureID = EnumCaptureID.CAPTURED.myName
                binding.rbDied.id -> captureID = EnumCaptureID.DIED.myName
                binding.rbEscaped.id -> captureID = EnumCaptureID.ESCAPED.myName
                binding.rbReleased.id -> captureID = EnumCaptureID.RELEASED.myName
                binding.rbNullCapture.id -> captureID = null
            }
        }
    }

    private fun generateCode(){
        val specieCode = binding.autoCompTvSpecie.text.toString()
        val trapID = binding.autoCompTvTrapId.text.toString()
        if (specieCode.isBlank() || specieCode == "Other" || specieCode == "PVP" || specieCode == "TRE" || specieCode == "C" || specieCode == "P"){
            Toast.makeText(requireContext(), "Choose a valiable specie code.", Toast.LENGTH_LONG).show()
            code = null
            binding.etCodeMouseAdd.setText("")
            return
        }
        if (captureID.isNullOrEmpty() || captureID == EnumCaptureID.ESCAPED.myName || captureID == EnumCaptureID.DIED.myName){
            Toast.makeText(requireContext(), "Choose a valiable capture ID.", Toast.LENGTH_LONG).show()
            code = null
            binding.etCodeMouseAdd.setText("")
            return
        }
        if (trapID.isBlank() || trapID == EnumTrapID.SNAP_TRAPS.myName){
            Toast.makeText(requireContext(), "Choose a valiable trap ID.", Toast.LENGTH_LONG).show()
            code = null
            binding.etCodeMouseAdd.setText("")
            return
        }

        listSpecie.forEach {
            if (it.speciesCode == specieCode){
                specie = it
            }
        }
        userViewModel.getActiveUser()
    }

    private fun setListenerToTrapID(){
        binding.autoCompTvTrapId.setOnItemClickListener { parent, view, position, id ->
            when (parent.getItemAtPosition(position) as String) {
                EnumTrapID.LIVE_TRAPS.myName -> {
                    binding.etBody.isEnabled = false
                    binding.etBody.setText("")
                    binding.etTail.isEnabled = false
                    binding.etTail.setText("")
                    binding.etFeet.isEnabled = false
                    binding.etFeet.setText("")
                    binding.etEar.isEnabled = false
                    binding.etEar.setText("")

                    binding.btnGenCode.isEnabled = true
                    binding.etCodeMouseAdd.isEnabled = true
                    binding.rgCaptureId.children.forEach {
                        it.isEnabled = true
                    }
                }
                EnumTrapID.SNAP_TRAPS.myName -> {
                    binding.btnGenCode.isEnabled = false
                    binding.etCodeMouseAdd.isEnabled = false
                    binding.etCodeMouseAdd.setText("")
                    binding.rgCaptureId.children.forEach {
                        it.isEnabled = false
                    }
                    binding.rgCaptureId.clearCheck()

                    binding.etBody.isEnabled = true
                    binding.etTail.isEnabled = true
                    binding.etFeet.isEnabled = true
                    binding.etEar.isEnabled = true
                }
            }
        }
    }

    private fun hideNonMaleFields(){
        binding.cbGravit.isEnabled = false
        binding.cbGravit.isChecked = false
        binding.cbLactating.isEnabled = false
        binding.cbLactating.isChecked = false
        binding.cbMc.isEnabled = false
        binding.cbMc.isChecked = false
        binding.etMcRight.isEnabled = false
        binding.etMcRight.setText("")
        binding.etMcLeft.isEnabled = false
        binding.etMcLeft.setText("")
        binding.etEmbryoRight.isEnabled = false
        binding.etEmbryoRight.setText("")
        binding.etEmbryoLeft.isEnabled = false
        binding.etEmbryoLeft.setText("")
        binding.etEmbryoDiameter.isEnabled = false
        binding.etEmbryoDiameter.setText("")
    }

    private fun showNonMaleFields(){
        binding.cbGravit.isEnabled = true
        binding.cbLactating.isEnabled = true
        binding.cbMc.isEnabled = true
        binding.etMcRight.isEnabled = true
        binding.etMcLeft.isEnabled = true
        binding.etEmbryoRight.isEnabled = true
        binding.etEmbryoLeft.isEnabled = true
        binding.etEmbryoDiameter.isEnabled = true
    }

    private fun showDrawnRat(){
        if (binding.autoCompTvSpecie.text.toString().isBlank()) {
            Toast.makeText(requireContext(), "Select a valid Specie", Toast.LENGTH_LONG).show()
            return
        }
        speciesID = mapSpecie.getValue(binding.autoCompTvSpecie.text.toString())

        if (speciesID > 0 && code != null && code!! > 0 && code.toString().length < 5){
            val fragman = requireActivity().supportFragmentManager
            val floatFrag = DrawnFragment(code!!, specie?.upperFingers!!)
            floatFrag.show(fragman, "FloatFragMouseCode")
        }else{
            Toast.makeText(requireContext(), "Generate a valid code.", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToCamera() {
        val action = AddNewMouseFragmentDirections.actionAddNewMouseFragmentToTakePhotoFragment("Mouse", null)
        findNavController().navigate(action)
    }

    private fun insertMouse() {
        val trapID: String = binding.autoCompTvTrapId.text.toString()
        speciesID = mapSpecie.getOrDefault(binding.autoCompTvSpecie.text.toString(), 1)

        if (checkInput(speciesID, trapID)){
            code = if (trapID == EnumTrapID.SNAP_TRAPS.myName) null else giveOutPutInt(binding.etCodeMouseAdd.text.toString())
            captureID = if (trapID == EnumTrapID.SNAP_TRAPS.myName) null else captureID

            val protocolID: Long? = mapProtocol.getOrDefault(binding.autoCompTvProtocol.text.toString(), null)
            val sexActive: Boolean? = binding.cbSexActive.isChecked
            val weight: Float? = giveOutPutFloat(binding.etWeight.text.toString())

            body = if (trapID == EnumTrapID.LIVE_TRAPS.myName) null else giveOutPutFloat(binding.etBody.text.toString())
            tail = if (trapID == EnumTrapID.LIVE_TRAPS.myName) null else giveOutPutFloat(binding.etTail.text.toString())
            feet = if (trapID == EnumTrapID.LIVE_TRAPS.myName) null else giveOutPutFloat(binding.etFeet.text.toString())
            ear = if (trapID == EnumTrapID.LIVE_TRAPS.myName) null else giveOutPutFloat(binding.etEar.text.toString())

            val testesLength: Float? = giveOutPutFloat(binding.etTestesLength.text.toString())
            val testesWidth: Float? = giveOutPutFloat(binding.etTestesWidth.text.toString())

            gravitidy = if (sex == EnumSex.MALE.myName) null else binding.cbGravit.isChecked
            lactating = if (sex == EnumSex.MALE.myName) null else binding.cbLactating.isChecked
            // počet embryí v oboch rohoch maternice a ich priemer
            embryoRight = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            embryoLeft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            embryoDiameter = if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            MC = if (sex == EnumSex.MALE.myName) null else binding.cbMc.isChecked
            // počet placentálnych polypov
            MCright = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
            MCleft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())

            val note: String? = if (binding.etMouseNote.text.toString().isBlank()) null else binding.etMouseNote.text.toString()
            val deviceID: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

            val mouse = Mouse(0, code, deviceID, null, speciesID, protocolID, args.occasion.occasionId,
                args.occasion.localityID, trapID, Calendar.getInstance().time, null, sex, age, gravitidy, lactating, sexActive,
                weight, recapture = false, captureID, body, tail, feet, ear, testesLength, testesWidth, embryoRight, embryoLeft,
                embryoDiameter, MC, MCright, MCleft, note, imgName)

            // ulozit mys
            var selSpecie: Specie? = null
            listSpecie.forEach {
                if (it.specieId == mouse.speciesID)
                    selSpecie = it
            }

            if (mouse.weight != null && selSpecie?.minWeight != null && selSpecie?.maxWeight != null) {
                checkWeightAndSave(mouse)
            } else {
                executeTask(mouse)
            }

        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse) {
        if (mouse.weight!! > specie?.maxWeight!! || mouse.weight!! < specie?.minWeight!!){
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){_, _ ->
                executeTask(mouse)
            }
                .setNegativeButton("No"){_, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        }else{
            executeTask(mouse)
        }
    }

    private fun executeTask(mouse: Mouse){
        // zvacsit numMice projektu do ktoreho sa pridava tato mys
        updateProjectNumMice()

        // zvacsit numMice occasion do ktorej sa pridava tato mys
        updateOccasionNumMice()

        mouseViewModel.insertMouse(mouse)

        Toast.makeText(requireContext(), "New mouse added.", Toast.LENGTH_SHORT).show()

        findNavController().navigateUp()
    }

    private fun giveOutPutInt(input: String?): Int?{
        return if (input.isNullOrBlank() || input == "null") null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float?{
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

    private fun updateProjectNumMice(){
        val session: Session = sessionViewModel.getSession(args.occasion.sessionID)
        val updatedProject: Project = projectViewModel.getProject(session.projectID!!)

        updatedProject.numMice = (updatedProject.numMice + 1)
        updatedProject.projectDateTimeUpdated = Calendar.getInstance().time

        projectViewModel.updateProject(updatedProject)
    }

    private fun updateOccasionNumMice(){
        val updatedOccasion: Occasion = args.occasion

        updatedOccasion.numMice = (updatedOccasion.numMice?.plus(1))
        updatedOccasion.occasionDateTimeUpdated = Calendar.getInstance().time

        occasionViewModel.updateOccasion(updatedOccasion)
    }

    private fun checkInput(specieID: Long, trapID: String): Boolean {
        return specieID > 0 && trapID.isNotEmpty()
    }

}