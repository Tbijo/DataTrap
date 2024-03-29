package com.example.datatrap.mouse.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.example.datatrap.myenums.EnumSpecie
import com.example.datatrap.viewmodels.*
import com.example.datatrap.viewmodels.datastore.PrefViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddNewMouseFragment : Fragment() {

    private var _binding: FragmentAddNewMouseBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<AddNewMouseFragmentArgs>()
    private val mouseViewModel: MouseViewModel by viewModels()
    private val specieViewModel: SpecieViewModel by viewModels()
    private val protocolViewModel: ProtocolViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    private lateinit var listSpecie: List<SpecSelectList>
    private lateinit var listProtocol: List<Protocol>
    private lateinit var activeCodeList: List<Int>
    private lateinit var trapsList: List<Int>

    private var oldCode: Int = 0
    private var code: Int? = null
    private var sex: String? = null
    private var age: String? = null
    private var captureID: String? = null
    private var speciesID: Long = 0
    private var specie: SpecSelectList? = null
    private var protocolID: Long? = null
    private var team: Int = -1

    private var mouseId: Long = 0
    var deviceID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNewMouseBinding.inflate(inflater, container, false)

        deviceID = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        fillDropDown()

        mouseViewModel.countMiceForLocality(args.occList.localityID).observe(viewLifecycleOwner) {
            oldCode = it + 1
        }

        binding.btnGenCode.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                generateCode()
            }
        }

        setListeners()

        mouseViewModel.getActiveCodeOfLocality(args.occList.localityID, Calendar.getInstance().time.time
        ).observe(viewLifecycleOwner) { codeList ->
            activeCodeList = codeList
        }

        // get selected Team
        prefViewModel.readUserTeamPref.observe(viewLifecycleOwner) {
            team = it
        }

        // get traps
        mouseViewModel.getTrapsIdInOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            trapsList = it
        }

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        fillDropDown()

        val dropDownArrTrapID = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_names,
            (1..args.occList.numTraps).toList()
        )
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
        when (item.itemId) {
            R.id.menu_save -> insertMouse()
            R.id.menu_rat -> showDrawnRat()
            R.id.menu_camera -> goToCamera()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fillDropDown() {
        specieViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
            listSpecie = it
            val listCode = mutableListOf<String>()
            it.forEach { specie ->
                listCode.add(specie.speciesCode)
            }
            val dropDownArrSpecie =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, listCode)
            binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)
        }

        protocolViewModel.procolList.observe(viewLifecycleOwner) {
            listProtocol = it
            val listProtName = mutableListOf<String>()
            listProtName.add("null")
            it.forEach { protocol ->
                listProtName.add(protocol.protocolName)
            }
            val dropDownArrProtocol =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, listProtName)
            binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)
        }
    }

    private fun setListeners() {
        binding.autoCompTvSpecie.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            specie = listSpecie.first {
                it.speciesCode == name
            }
            speciesID = specie!!.specieId
        }

        binding.autoCompTvProtocol.setOnItemClickListener { parent, _, position, _ ->
            val name: String = parent.getItemAtPosition(position) as String
            protocolID = listProtocol.firstOrNull {
                it.protocolName == name
            }?.protocolId
        }

        binding.rgSex.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId) {
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

        binding.rgAge.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId) {
                binding.rbAdult.id -> age = EnumMouseAge.ADULT.myName
                binding.rbJuvenile.id -> age = EnumMouseAge.JUVENILE.myName
                binding.rbSubadult.id -> age = EnumMouseAge.SUBADULT.myName
                binding.rbNullAge.id -> age = null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { _, radioButtonId ->
            when (radioButtonId) {
                binding.rbCaptured.id -> captureID = EnumCaptureID.CAPTURED.myName
                binding.rbDied.id -> captureID = EnumCaptureID.DIED.myName
                binding.rbEscaped.id -> captureID = EnumCaptureID.ESCAPED.myName
                binding.rbReleased.id -> captureID = EnumCaptureID.RELEASED.myName
                binding.rbNullCapture.id -> captureID = null
            }
        }
    }

    private fun generateCode() {
        if (specie == null || specie!!.speciesCode == EnumSpecie.Other.name || specie!!.speciesCode == EnumSpecie.PVP.name || specie!!.speciesCode == EnumSpecie.TRE.name || specie!!.speciesCode == EnumSpecie.C.name || specie!!.speciesCode == EnumSpecie.P.name) {
            Toast.makeText(requireContext(), "Choose a valiable specie code.", Toast.LENGTH_LONG)
                .show()
            code = null
            binding.etCodeMouseAdd.setText("")
            return
        }

        if (captureID.isNullOrEmpty() || captureID == EnumCaptureID.ESCAPED.myName || captureID == EnumCaptureID.DIED.myName) {
            Toast.makeText(requireContext(), "Choose a valiable capture ID.", Toast.LENGTH_LONG)
                .show()
            code = null
            binding.etCodeMouseAdd.setText("")
            return
        }

        val codeGen =
            CodeGenerator(oldCode, specie?.upperFingers!!, team, activeCodeList)
        code = codeGen.generateCode()
        if (code == 0) {
            Toast.makeText(requireContext(), "No code is available.", Toast.LENGTH_LONG).show()
        }
        binding.etCodeMouseAdd.setText(code.toString())

    }

    private fun hideNonMaleFields() {
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

    private fun showNonMaleFields() {
        binding.cbGravit.isEnabled = true
        binding.cbLactating.isEnabled = true
        binding.cbMc.isEnabled = true
        binding.etMcRight.isEnabled = true
        binding.etMcLeft.isEnabled = true
        binding.etEmbryoRight.isEnabled = true
        binding.etEmbryoLeft.isEnabled = true
        binding.etEmbryoDiameter.isEnabled = true
    }

    private fun showDrawnRat() {
        if (binding.autoCompTvSpecie.text.toString().isBlank()) {
            Toast.makeText(requireContext(), "Select a valid Specie", Toast.LENGTH_LONG).show()
            return
        }

        if (speciesID > 0 && code != null && code!! > 0 && code.toString().length < 5) {
            val fragman = requireActivity().supportFragmentManager
            val floatFrag = DrawnFragment(code!!, specie?.upperFingers!!)
            floatFrag.show(fragman, "FloatFragMouseCode")
        } else {
            Toast.makeText(requireContext(), "Generate a valid code.", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToCamera() {
        if (mouseId <= 0) {
            Toast.makeText(requireContext(), "You need to insert a mouse first.", Toast.LENGTH_LONG)
                .show()
            return
        }

        val action = AddNewMouseFragmentDirections.actionAddNewMouseFragmentToTakePhotoFragment(
            "Mouse",
            mouseId,
            deviceID
        )
        findNavController().navigate(action)
    }

    private fun insertMouse() {
        if (mouseId > 0) {
            Toast.makeText(requireContext(), "Mouse already inserted.", Toast.LENGTH_LONG).show()
            return
        }

        if (speciesID > 0) {
            code = giveOutPutInt(binding.etCodeMouseAdd.text.toString())

            if (code in activeCodeList) {
                Toast.makeText(requireContext(), "Code is not available.", Toast.LENGTH_LONG).show()
                return
            }

            val sexActive: Boolean = binding.cbSexActive.isChecked
            val weight: Float? = giveOutPutFloat(binding.etWeight.text.toString())
            val trapID: Int? = giveOutPutInt(binding.autoCompTvTrapId.text.toString())

            val body = giveOutPutFloat(binding.etBody.text.toString())
            val tail = giveOutPutFloat(binding.etTail.text.toString())
            val feet = giveOutPutFloat(binding.etFeet.text.toString())
            val ear = giveOutPutFloat(binding.etEar.text.toString())

            val testesLength: Float? = giveOutPutFloat(binding.etTestesLength.text.toString())
            val testesWidth: Float? = giveOutPutFloat(binding.etTestesWidth.text.toString())

            val gravitidy = binding.cbGravit.isChecked
            val lactating = binding.cbLactating.isChecked
            // počet embryí v oboch rohoch maternice a ich priemer
            val embryoRight =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            val embryoLeft =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            val embryoDiameter =
                if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            val MC = binding.cbMc.isChecked
            // počet placentálnych polypov
            val MCright =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
            val MCleft =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())

            val note: String? = if (binding.etMouseNote.text.toString()
                    .isBlank()
            ) null else binding.etMouseNote.text.toString()

            val mouse = Mouse(
                0,
                0,
                code,
                deviceID,
                null,
                speciesID,
                protocolID,
                args.occList.occasionId,
                args.occList.localityID,
                trapID,
                Calendar.getInstance().time,
                null,
                sex,
                age,
                gravitidy,
                lactating,
                sexActive,
                weight,
                recapture = false,
                captureID,
                body,
                tail,
                feet,
                ear,
                testesLength,
                testesWidth,
                embryoRight,
                embryoLeft,
                embryoDiameter,
                MC,
                MCright,
                MCleft,
                note,
                Calendar.getInstance().time.time
            )

            // ulozit mys
            if (mouse.weight != null && specie!!.minWeight != null && specie!!.maxWeight != null) {
                checkWeightAndSave(mouse)
            }
            else if (mouse.trapID != null && mouse.trapID in trapsList) {
                checkTrapAvailability(mouse)
            }
            else {
                executeTask(mouse)
            }

        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkWeightAndSave(mouse: Mouse) {
        if (mouse.weight!! > specie?.maxWeight!! || mouse.weight!! < specie?.minWeight!!) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                checkTrapAvailability(mouse)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        } else {
            checkTrapAvailability(mouse)
        }
    }

    private fun checkTrapAvailability(mouse: Mouse) {
        if (mouse.trapID != null && mouse.trapID in trapsList) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                executeTask(mouse)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Trap In Use")
                .setMessage("Selected trap is in use, save anyway?")
                .create().show()
        } else {
            executeTask(mouse)
        }
    }

    private fun executeTask(mouse: Mouse) {
        mouseViewModel.insertMouse(mouse)

        Toast.makeText(requireContext(), "New mouse added.", Toast.LENGTH_SHORT).show()

        mouseViewModel.mouseId.observe(viewLifecycleOwner) {
            mouseId = it
        }
    }

    private fun giveOutPutInt(input: String?): Int? {
        return if (input.isNullOrBlank() || input == "null") null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float? {
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

}