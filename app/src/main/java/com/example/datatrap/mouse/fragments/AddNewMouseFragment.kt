package com.example.datatrap.mouse.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.datatrap.R
import com.example.datatrap.databinding.FragmentAddNewMouseBinding
import com.example.datatrap.models.*
import com.example.datatrap.models.tuples.SpecSelectList
import com.example.datatrap.mouse.fragments.generator.CodeGenerator
import com.example.datatrap.myenums.EnumCaptureID
import com.example.datatrap.myenums.EnumMouseAge
import com.example.datatrap.myenums.EnumSex
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
    private lateinit var userViewModel: UserViewModel

    private lateinit var listSpecie: List<SpecSelectList>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var activeCodeList: List<Int>

    private var oldCode: Int = 0
    private var code: Int? = null
    private var sex: String? = null
    private var imgName: String? = null
    private var age: String? = null
    private var captureID: String? = null
    private var speciesID: Long = 0
    private var specie: SpecSelectList? = null
    private var protocolID: Long? = null
    private lateinit var currentUser: User

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
        mouseViewModel = ViewModelProvider(this).get(MouseViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        specieViewModel = ViewModelProvider(this).get(SpecieViewModel::class.java)
        protocolViewModel = ViewModelProvider(this).get(ProtocolViewModel::class.java)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.dataToShare.observe(requireActivity(), {
            imgName = it
        })

        fillDropDown()

        mouseViewModel.countMiceForLocality(args.occList.localityID).observe(viewLifecycleOwner, {
            oldCode = it + 1
        })

        binding.btnGenCode.setOnClickListener {
            generateCode()
        }

        setListeners()

        mouseViewModel.getActiveCodeOfLocality(args.occList.localityID, Calendar.getInstance().time.time, MILLISECONDS_IN_2_YEAR).observe(viewLifecycleOwner, { codeList ->
            activeCodeList = codeList
        })

        // get user for
        userViewModel.getActiveUser().observe(viewLifecycleOwner, {
            currentUser = it
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        fillDropDown()

        val dropDownArrTrapID = ArrayAdapter(requireContext(), R.layout.dropdown_names, (1..args.occList.numTraps).toList())
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
        specieViewModel.getSpeciesForSelect().observe(viewLifecycleOwner, {
            listSpecie = it
            val listCode = mutableListOf<String>()
            it.forEach { specie ->
                listCode.add(specie.speciesCode)
            }
            val dropDownArrSpecie = ArrayAdapter(requireContext(), R.layout.dropdown_names, listCode)
            binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)
        })

        protocolViewModel.procolList.observe(viewLifecycleOwner, {
            listProtocol = it
            val listProtName = mutableListOf<String>()
            listProtName.add("null")
            it.forEach { protocol ->
                listProtName.add(protocol.protocolName)
            }
            val dropDownArrProtocol = ArrayAdapter(requireContext(), R.layout.dropdown_names, listProtName)
            binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)
        })
    }

    private fun setListeners() {
        binding.autoCompTvSpecie.setOnItemClickListener { parent, view, position, id ->
            val name: String = parent.getItemAtPosition(position) as String
            specie = listSpecie.first {
                it.speciesCode == name
            }
            speciesID = specie!!.specieId
        }

        binding.autoCompTvProtocol.setOnItemClickListener { parent, view, position, id ->
            val name: String = parent.getItemAtPosition(position) as String
            protocolID = listProtocol.firstOrNull {
                it.protocolName == name
            }?.protocolId
        }

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

    private fun generateCode() {
        if (specie == null || specie!!.speciesCode == "Other" || specie!!.speciesCode == "PVP" || specie!!.speciesCode == "TRE" || specie!!.speciesCode == "C" || specie!!.speciesCode == "P") {
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

        if (currentUser != null) {
            val codeGen = CodeGenerator(oldCode, specie?.upperFingers!!, currentUser.team, activeCodeList)
            code = codeGen.generateCode()
            if(code == 0) {
                Toast.makeText(requireContext(), "No code is available.", Toast.LENGTH_LONG).show()
            }
            binding.etCodeMouseAdd.setText(code.toString())
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
        if (speciesID > 0) {
            code = giveOutPutInt(binding.etCodeMouseAdd.text.toString())
            if (code in activeCodeList) {
                Toast.makeText(requireContext(), "Code is not available.", Toast.LENGTH_LONG).show()
                return
            }

            val sexActive: Boolean? = binding.cbSexActive.isChecked
            val weight: Float? = giveOutPutFloat(binding.etWeight.text.toString())
            val trapID: Int? = giveOutPutInt(binding.autoCompTvTrapId.text.toString())

            val body = giveOutPutFloat(binding.etBody.text.toString())
            val tail = giveOutPutFloat(binding.etTail.text.toString())
            val feet = giveOutPutFloat(binding.etFeet.text.toString())
            val ear = giveOutPutFloat(binding.etEar.text.toString())

            val testesLength: Float? = giveOutPutFloat(binding.etTestesLength.text.toString())
            val testesWidth: Float? = giveOutPutFloat(binding.etTestesWidth.text.toString())

            val gravitidy = if (sex == EnumSex.MALE.myName) null else binding.cbGravit.isChecked
            val lactating = if (sex == EnumSex.MALE.myName) null else binding.cbLactating.isChecked
            // počet embryí v oboch rohoch maternice a ich priemer
            val embryoRight = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            val embryoLeft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            val embryoDiameter = if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            val MC = if (sex == EnumSex.MALE.myName) null else binding.cbMc.isChecked
            // počet placentálnych polypov
            val MCright = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
            val MCleft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())

            val note: String? = if (binding.etMouseNote.text.toString().isBlank()) null else binding.etMouseNote.text.toString()
            val deviceID: String = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)

            val mouse = Mouse(0, code, deviceID, null, speciesID, protocolID, args.occList.occasionId,
                args.occList.localityID, trapID, Calendar.getInstance().time, null, sex, age, gravitidy, lactating, sexActive,
                weight, recapture = false, captureID, body, tail, feet, ear, testesLength, testesWidth, embryoRight, embryoLeft,
                embryoDiameter, MC, MCright, MCleft, note, imgName)

            // ulozit mys
            if (mouse.weight != null && specie!!.minWeight != null && specie!!.maxWeight != null) {
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

}