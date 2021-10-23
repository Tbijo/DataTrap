package com.example.datatrap.mouse.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.provider.Settings
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
import com.example.datatrap.models.*
import com.example.datatrap.mouse.fragments.generator.CodeGenerator
import com.example.datatrap.myenums.EnumCaptureID
import com.example.datatrap.myenums.EnumMouseAge
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
    private lateinit var listProtocol: List<Protocol>
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
        listSpecie = specieViewModel.specieList.value!!
        listSpecie.forEach {
            mapSpecie[it.speciesCode] = it.specieId
        }

        protocolViewModel = ViewModelProvider(this).get(ProtocolViewModel::class.java)
        listProtocol = protocolViewModel.procolList.value!!
        listProtocol.forEach {
            mapProtocol[it.protocolName] = it.protocolId
        }
        mapProtocol["null"] = null

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

        oldCode = mouseViewModel.countMiceForLocality(args.occasion.localityID) + 1

        binding.btnGenCode.setOnClickListener {
            generateCode()
        }

        setListeners()
        setListenerToTrapID()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val dropDownArrSpecie = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapSpecie.keys.toList())
        binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

        val dropDownArrProtocol = ArrayAdapter(requireContext(), R.layout.dropdown_names, mapProtocol.keys.toList())
        binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)

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

    private fun setListeners(){
        binding.rgSex.setOnCheckedChangeListener{ radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbMale.id -> {
                    sex = "Male"
                    hideNonMaleFields()
                }
                binding.rbFemale.id -> {
                    sex = "Female"
                    showNonMaleFields()
                }
                binding.rbNullSex.id -> {
                    sex = null
                    showNonMaleFields()
                }
            }
        }

        binding.rgAge.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbAdult.id -> age =  EnumMouseAge.ADULT.myName
                binding.rbJuvenile.id -> age =  EnumMouseAge.JUVENILE.myName
                binding.rbSubadult.id -> age =  EnumMouseAge.SUBADULT.myName
                binding.rbNullAge.id -> age =  null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { radioGroup, radioButtonId ->
            when(radioButtonId){
                binding.rbCaptured.id -> captureID =  EnumCaptureID.CAPTURED.myName
                binding.rbDied.id -> captureID =  EnumCaptureID.DIED.myName
                binding.rbEscaped.id -> captureID =  EnumCaptureID.ESCAPED.myName
                binding.rbReleased.id -> captureID =  EnumCaptureID.RELEASED.myName
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
        val team: Int = userViewModel.getActiveUser()?.team!!
        val codeGen = CodeGenerator(this, oldCode, specie?.upperFingers!!, team, args.occasion.localityID)
        code = codeGen.generateCode()
        if(code == 0){
            Toast.makeText(requireContext(), "No code is available.", Toast.LENGTH_LONG).show()
        }
        binding.etCodeMouseAdd.setText(code.toString())
    }

    private fun setListenerToTrapID(){
        binding.autoCompTvTrapId.setOnItemClickListener { parent, view, position, id ->
            when (parent.getItemAtPosition(position) as String) {
                EnumTrapID.LIVE_TRAPS.myName -> {
                    body = null
                    tail = null
                    feet = null
                    ear = null
                    binding.etBody.visibility = View.INVISIBLE
                    binding.etBody.setText("")
                    binding.etTail.visibility = View.INVISIBLE
                    binding.etTail.setText("")
                    binding.etFeet.visibility = View.INVISIBLE
                    binding.etFeet.setText("")
                    binding.etEar.visibility = View.INVISIBLE
                    binding.etEar.setText("")

                    binding.btnGenCode.visibility = View.VISIBLE
                    binding.etCodeMouseAdd.visibility = View.VISIBLE
                    binding.rgCaptureId.visibility = View.VISIBLE
                }
                EnumTrapID.SNAP_TRAPS.myName -> {
                    code = null
                    captureID = null
                    binding.btnGenCode.visibility = View.INVISIBLE
                    binding.etCodeMouseAdd.visibility = View.INVISIBLE
                    binding.etCodeMouseAdd.setText("")
                    binding.rgCaptureId.visibility = View.INVISIBLE
                    binding.rgCaptureId.clearCheck()

                    binding.etBody.visibility = View.VISIBLE
                    binding.etTail.visibility = View.VISIBLE
                    binding.etFeet.visibility = View.VISIBLE
                    binding.etEar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun hideNonMaleFields(){
        gravitidy = null
        lactating = null
        embryoRight = null
        embryoLeft = null
        embryoDiameter = null
        MC = null
        MCright = null
        MCleft = null
        binding.cbGravit.visibility = View.INVISIBLE
        binding.cbGravit.isChecked = false
        binding.cbLactating.visibility = View.INVISIBLE
        binding.cbLactating.isChecked = false
        binding.cbMc.visibility = View.INVISIBLE
        binding.cbMc.isChecked = false
        binding.etMcRight.visibility = View.INVISIBLE
        binding.etMcRight.setText("")
        binding.etMcLeft.visibility = View.INVISIBLE
        binding.etMcLeft.setText("")
        binding.etEmbryoRight.visibility = View.INVISIBLE
        binding.etEmbryoRight.setText("")
        binding.etEmbryoLeft.visibility = View.INVISIBLE
        binding.etEmbryoLeft.setText("")
        binding.etEmbryoDiameter.visibility = View.INVISIBLE
        binding.etEmbryoDiameter.setText("")
    }

    private fun showNonMaleFields(){
        binding.cbGravit.visibility = View.VISIBLE
        binding.cbLactating.visibility = View.VISIBLE
        binding.cbMc.visibility = View.VISIBLE
        binding.etMcRight.visibility = View.VISIBLE
        binding.etMcLeft.visibility = View.VISIBLE
        binding.etEmbryoRight.visibility = View.VISIBLE
        binding.etEmbryoLeft.visibility = View.VISIBLE
        binding.etEmbryoDiameter.visibility = View.VISIBLE
    }

    private fun showDrawnRat(){
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
        speciesID = mapSpecie.getValue(binding.autoCompTvSpecie.text.toString())

        if (checkInput(speciesID, trapID)){
            code = if (code != null) null else giveOutPutInt(binding.etCodeMouseAdd.text.toString())
            val protocolID: Long? = mapProtocol.getValue(binding.autoCompTvProtocol.text.toString())
            val sexActive: Boolean? = binding.cbSexActive.isChecked
            val weight: Float? = giveOutPutFloat(binding.etWeight.text.toString())
            body = if (body == null) null else giveOutPutFloat(binding.etBody.text.toString())
            tail = if (tail == null) null else giveOutPutFloat(binding.etTail.text.toString())
            feet = if (feet == null) null else giveOutPutFloat(binding.etFeet.text.toString())
            ear = if (ear == null) null else giveOutPutFloat(binding.etEar.text.toString())
            val testesLength: Float? = giveOutPutFloat(binding.etTestesLength.text.toString())
            val testesWidth: Float? = giveOutPutFloat(binding.etTestesWidth.text.toString())
            gravitidy = if (gravitidy == null) null else { binding.cbGravit.isChecked }
            lactating = if (lactating == null) null else { binding.cbLactating.isChecked }
            //počet embryí v oboch rohoch maternice a ich priemer
            embryoRight = if (embryoRight == null) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            embryoLeft = if (embryoLeft == null) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            embryoDiameter = if (embryoDiameter == null) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            MC = if (MC == null) null else { binding.cbMc.isChecked }
            //počet placentálnych polypov
            MCright = if (MCright == null) null else giveOutPutInt(binding.etMcRight.text.toString())
            MCleft = if (MCleft == null) null else giveOutPutInt(binding.etMcLeft.text.toString())
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
        return if (input.isNullOrBlank()) null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float?{
        return if (input.isNullOrBlank()) null else Integer.parseInt(input).toFloat()
    }

    private fun updateProjectNumMice(){
        val session: Session = sessionViewModel.getSession(args.occasion.sessionID)!!
        val updatedProject: Project = projectViewModel.getProject(session.projectID!!)!!

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