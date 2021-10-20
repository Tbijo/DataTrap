package com.example.datatrap.mouse.fragments

import android.app.AlertDialog
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
import com.example.datatrap.databinding.FragmentUpdateMouseBinding
import com.example.datatrap.models.*
import com.example.datatrap.myenums.EnumCaptureID
import com.example.datatrap.myenums.EnumMouseAge
import com.example.datatrap.myenums.EnumTrapID
import com.example.datatrap.viewmodels.*
import java.util.*

class UpdateMouseFragment : Fragment() {

    private var _binding: FragmentUpdateMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateMouseFragmentArgs>()
    private lateinit var mouseViewModel: MouseViewModel
    private lateinit var specieViewModel: SpecieViewModel
    private lateinit var protocolViewModel: ProtocolViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var sessionViewModel: SessionViewModel
    private lateinit var occasionViewModel: OccasionViewModel

    private lateinit var listSpecie: List<Specie>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var mapSpecie: MutableMap<String, Long>
    private lateinit var mapProtocol: MutableMap<String?, Long?>

    private var sex: String? = null
    private var imgName: String? = null
    private var age: String? = null
    private var captureID: String? = null
    private var gravitidy: Int? = null
    private var lactating: Int? = null
    private var embryoRight: Int? = null
    private var embryoLeft: Int? = null
    private var embryoDiameter: Float? = null
    private var MC: Int? = null
    private var MCright: Int? = null
    private var MCleft: Int? = null
    private var body: Float? = null
    private var tail: Float? = null
    private var feet: Float? = null
    private var ear: Float? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateMouseBinding.inflate(inflater, container, false)
        projectViewModel = ViewModelProvider(this).get(ProjectViewModel::class.java)
        sessionViewModel = ViewModelProvider(this).get(SessionViewModel::class.java)
        occasionViewModel = ViewModelProvider(this).get(OccasionViewModel::class.java)
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
        mapProtocol["null"] = null

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), Observer {
            imgName = it
        })

        setListeners()
        setListenerToTrapID()

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

        val dropDownArrTrapID = ArrayAdapter(requireContext(), R.layout.dropdown_names, EnumTrapID.myValues())
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateMouse()
            R.id.menu_camera -> goToCamera()
            R.id.menu_delete -> deleteMouse()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initMouseValuesToView(){
        binding.cbGravit.isChecked = args.mouse.gravidity == 1
        binding.cbLactating.isChecked = args.mouse.lactating == 1
        binding.cbSexActive.isChecked = args.mouse.sexActive == 1
        binding.cbMc.isChecked = args.mouse.MC == 1

        binding.etMouseCodeUpdate.setText(args.mouse.code.toString())
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
                showNonMaleFields()
            }
            "Female" -> {
                binding.rbFemale.isChecked = true
                hideNonMaleFields()
            }
            null -> binding.rbNullSex.isChecked = true
        }

        when(args.mouse.age){
            EnumMouseAge.ADULT.myName -> binding.rbAdult.isChecked = true
            EnumMouseAge.SUBADULT.myName -> binding.rbSubadult.isChecked = true
            EnumMouseAge.JUVENILE.myName -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when(args.mouse.captureID){
            EnumCaptureID.CAPTURED.myName -> binding.rbCaptured.isChecked = true
            EnumCaptureID.DIED.myName -> binding.rbDied.isChecked = true
            EnumCaptureID.ESCAPED.myName -> binding.rbEscaped.isChecked = true
            EnumCaptureID.RELEASED.myName -> binding.rbReleased.isChecked = true
            null -> binding.rbNullCapture.isChecked = true
        }
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

                    binding.rgCaptureId.visibility = View.VISIBLE
                }
                EnumTrapID.SNAP_TRAPS.myName -> {
                    captureID = null
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

    private fun goToCamera() {
        val action = UpdateMouseFragmentDirections.actionUpdateMouseFragmentToTakePhotoFragment("Mouse", args.mouse.imgName)
        findNavController().navigate(action)
    }

    private fun deleteMouse(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->
            // zmensit numMice projektu do ktoreho sa pridava tato mys
            updateProjectNumMice()

            // zmensit numMice occasion do ktorej sa pridava tato mys
            updateOccasionNumMice()

            mouseViewModel.deleteMouse(args.mouse)

            Toast.makeText(requireContext(),"Mouse deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Mouse?")
            .setMessage("Are you sure you want to delete this mouse?")
            .create().show()
    }

    private fun updateProjectNumMice(){
        val session: Session = sessionViewModel.getSession(args.occasion.sessionID)!!
        val updatedProject: Project = projectViewModel.getProject(session.projectID!!)!!
        updatedProject.numMice = (updatedProject.numMice - 1)
        projectViewModel.updateProject(updatedProject)
    }

    private fun updateOccasionNumMice(){
        val updatedOccasion: Occasion = args.occasion
        updatedOccasion.numMice = (updatedOccasion.numMice?.minus(1))
        occasionViewModel.updateOccasion(updatedOccasion)
    }

    private fun updateMouse() {
        val trapID: String = binding.autoCompTvTrapId.text.toString()
        val speciesID: Long = mapSpecie.getValue(binding.autoCompTvSpecie.text.toString())

        if (checkInput(speciesID, trapID)){
            val mouse: Mouse = args.mouse
            mouse.speciesID = speciesID
            mouse.trapID = trapID
            mouse.sex = sex
            mouse.age = age
            mouse.captureID = captureID
            mouse.mouseDateTimeUpdated = Calendar.getInstance().time

            mouse.protocolID = mapProtocol.getValue(binding.autoCompTvProtocol.text.toString())
            mouse.sexActive = if (binding.cbSexActive.isChecked) 1 else 0
            mouse.weight = giveOutPutFloat(binding.etWeight.text.toString())
            mouse.body = if (body == null) null else giveOutPutFloat(binding.etBody.text.toString())
            mouse.tail = if (tail == null) null else giveOutPutFloat(binding.etTail.text.toString())
            mouse.feet = if (feet == null) null else giveOutPutFloat(binding.etFeet.text.toString())
            mouse.ear = if (ear == null) null else giveOutPutFloat(binding.etEar.text.toString())
            mouse.testesLength = giveOutPutFloat(binding.etTestesLength.text.toString())
            mouse.testesWidth = giveOutPutFloat(binding.etTestesWidth.text.toString())
            mouse.gravidity = if (gravitidy == null) null else { if (binding.cbGravit.isChecked) 1 else 0 }
            mouse.lactating = if (lactating == null) null else { if (binding.cbLactating.isChecked) 1 else 0 }
            //počet embryí v oboch rohoch maternice a ich priemer
            mouse.embryoRight = if (embryoRight == null) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            mouse.embryoLeft = if (embryoLeft == null) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            mouse.embryoDiameter = if (embryoDiameter == null) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            mouse.MC = if (MC == null) null else { if (binding.cbMc.isChecked) 1 else 0 }
            //počet placentálnych polypov
            mouse.MCright = if (MCright == null) null else giveOutPutInt(binding.etMcRight.text.toString())
            mouse.MCleft = if (MCleft == null) null else giveOutPutInt(binding.etMcLeft.text.toString())
            mouse.note = if (binding.etMouseNote.text.toString().isBlank()) null else binding.etMouseNote.text.toString()
            mouse.imgName = imgName

            // update mouse
            var selSpecie: Specie? = null
            listSpecie.forEach{
                if (it.specieId == mouse.speciesID){
                    selSpecie = it
                }
            }

            if (mouse.weight != null && selSpecie?.minWeight != null && selSpecie?.maxWeight != null) {
                checkWeightAndSave(mouse, selSpecie!!)
            } else {
                executeTask(mouse)
            }

        }else{
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse, selSpecie: Specie) {
        if (mouse.weight!! > selSpecie.maxWeight!! || mouse.weight!! < selSpecie.minWeight!!){
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

    private fun executeTask(mouse: Mouse) {
        mouseViewModel.updateMouse(mouse)

        Toast.makeText(requireContext(), "Mouse updated.", Toast.LENGTH_SHORT).show()

        findNavController().navigateUp()
    }

    private fun checkInput(specieID: Long, trapID: String): Boolean {
        return specieID > 0 && trapID.isNotEmpty()
    }

    private fun giveOutPutInt(input: String?): Int?{
        return if (input.isNullOrBlank()) null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float?{
        return if (input.isNullOrBlank()) null else Integer.parseInt(input).toFloat()
    }

}