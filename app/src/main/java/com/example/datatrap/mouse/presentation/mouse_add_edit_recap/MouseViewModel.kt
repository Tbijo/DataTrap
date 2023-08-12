package com.example.datatrap.mouse.presentation.mouse_add_edit_recap

import android.app.AlertDialog
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.datatrap.R
import com.example.datatrap.core.data.pref.PrefViewModel
import com.example.datatrap.core.util.EnumCaptureID
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.mouse.domain.util.CodeGenerator
import com.example.datatrap.mouse.presentation.mouse_list.MouseListViewModel
import com.example.datatrap.camera.data.mouse_image.MouseImageEntity
import com.example.datatrap.camera.presentation.CameraViewModel
import com.example.datatrap.settings.protocol.data.ProtocolEntity
import com.example.datatrap.settings.protocol.presentation.ProtocolViewModel
import com.example.datatrap.specie.domain.model.SpecSelectList
import com.example.datatrap.specie.presentation.specie_list.SpecieListViewModel
import java.util.Calendar

class MouseViewModel: ViewModel() {

    private val cameraViewModel: CameraViewModel by viewModels()

    private lateinit var trapList: List<Int>

    private lateinit var currentMouseEntity: MouseEntity
    private var mouseImageEntity: MouseImageEntity? = null

    private val mouseListViewModel: MouseListViewModel by viewModels()
    private val specieListViewModel: SpecieListViewModel by viewModels()
    private val protocolViewModel: ProtocolViewModel by viewModels()
    private val prefViewModel: PrefViewModel by viewModels()

    private lateinit var listSpecie: List<SpecSelectList>
    private lateinit var listProtocolEntity: List<ProtocolEntity>
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

    init {
        deviceID = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        fillDropDown()

        mouseListViewModel.countMiceForLocality(args.occList.localityID).observe(viewLifecycleOwner) {
            oldCode = it + 1
        }

        binding.btnGenCode.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                generateCode()
            }
        }

        setListeners()

        mouseListViewModel.getActiveCodeOfLocality(args.occList.localityID, Calendar.getInstance().time.time
        ).observe(viewLifecycleOwner) { codeList ->
            activeCodeList = codeList
        }

        // get selected Team
        prefViewModel.readUserTeamPref.observe(viewLifecycleOwner) {
            team = it
        }

        // get traps
        mouseListViewModel.getTrapsIdInOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            trapsList = it
        }

        // menu
        R.id.menu_save -> insertMouse()
        R.id.menu_rat -> showDrawnRat()
        R.id.menu_camera -> goToCamera()
        R.id.menu_save -> updateMouse()
        R.id.menu_delete -> deleteMouse()

        // Update
        mouseListViewModel.getMouse(args.mouseOccTuple.mouseId).observe(viewLifecycleOwner) {
            currentMouseEntity = it
            initMouseValuesToView(it)
            fillDropDown(it)

            cameraViewModel.getImageForMouse(currentMouseEntity.mouseIid, args.mouseOccTuple.deviceID).observe(viewLifecycleOwner) { image ->
                mouseImageEntity = image
            }
        }

        mouseListViewModel.getTrapsIdInOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            trapList = it
        }

        setListeners()

        // recapture
        mouseListViewModel.getMouse(args.recapMouse.mouseId).observe(viewLifecycleOwner) {
            currentMouseEntity = it
            initMouseValuesToView(it)
            fillDropDown(it)
        }

        mouseListViewModel.getTrapsIdInOccasion(args.occList.occasionId).observe(viewLifecycleOwner) {
            trapList = it
        }

        deviceID = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

        setListeners()
        R.id.menu_save -> recaptureMouse()
        R.id.menu_camera -> goToCamera()

    }

    fun onResume() {

        fillDropDown()

        val dropDownArrTrapID = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_names,
            (1..args.occList.numTraps).toList()
        )
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

        // Update
        mouseListViewModel.getMouse(args.mouseOccTuple.mouseId).observe(viewLifecycleOwner) {
            fillDropDown(it)
        }
        mouseListViewModel.getMouse(args.recapMouse.mouseId).observe(viewLifecycleOwner) {
            fillDropDown(it)
        }
    }

    private fun fillDropDown() {
        specieListViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
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
            listProtocolEntity = it
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
            protocolID = listProtocolEntity.firstOrNull {
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

            val mouseEntity = MouseEntity(
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
            if (mouseEntity.weight != null && specie!!.minWeight != null && specie!!.maxWeight != null) {
                checkWeightAndSave(mouseEntity)
            }
            else if (mouseEntity.trapID != null && mouseEntity.trapID in trapsList) {
                checkTrapAvailability(mouseEntity)
            }
            else {
                executeTask(mouseEntity)
            }

        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkWeightAndSave(mouseEntity: MouseEntity) {
        if (mouseEntity.weight!! > specie?.maxWeight!! || mouseEntity.weight!! < specie?.minWeight!!) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                checkTrapAvailability(mouseEntity)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        } else {
            checkTrapAvailability(mouseEntity)
        }
    }

    private fun checkTrapAvailability(mouseEntity: MouseEntity) {
        if (mouseEntity.trapID != null && mouseEntity.trapID in trapsList) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                executeTask(mouseEntity)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Trap In Use")
                .setMessage("Selected trap is in use, save anyway?")
                .create().show()
        } else {
            executeTask(mouseEntity)
        }
    }

    private fun executeTask(mouseEntity: MouseEntity) {
        mouseListViewModel.insertMouse(mouseEntity)

        Toast.makeText(requireContext(), "New mouse added.", Toast.LENGTH_SHORT).show()

        mouseListViewModel.mouseId.observe(viewLifecycleOwner) {
            mouseId = it
        }
    }

    private fun giveOutPutInt(input: String?): Int? {
        return if (input.isNullOrBlank() || input == "null") null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float? {
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

    private fun fillDropDown(mouseEntity: MouseEntity) {
        specieListViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
            listSpecie = it
            val listCode = mutableListOf<String>()
            it.forEach { specie ->
                listCode.add(specie.speciesCode)
            }
            val dropDownArrSpecie =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, listCode)
            binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

            // init values
            specie = it.first { specie ->
                specie.specieId == mouseEntity.speciesID
            }
            speciesID = specie?.specieId
            binding.autoCompTvSpecie.setText(specie?.speciesCode, false)

        }

        protocolViewModel.procolList.observe(viewLifecycleOwner) {
            listProtocolEntity = it
            val listProtName = mutableListOf<String>()
            listProtName.add("null")
            it.forEach { protocol ->
                listProtName.add(protocol.protocolName)
            }
            val dropDownArrProtocol =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, listProtName)
            binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)

            // init values
            val protocol = it.firstOrNull { protocol ->
                protocol.protocolId == mouseEntity.protocolID
            }
            protocolID = protocol?.protocolId
            binding.autoCompTvProtocol.setText(protocol.toString(), false)

        }

        val dropDownArrTrapID = ArrayAdapter(requireContext(), R.layout.dropdown_names, (1..args.occList.numTraps).toList())
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

        binding.autoCompTvTrapId.setText(mouseEntity.trapID.toString(), false)
    }

    private fun initMouseValuesToView(mouseEntity: MouseEntity) {
        binding.cbGravit.isChecked = mouseEntity.gravidity == true
        binding.cbLactating.isChecked = mouseEntity.lactating == true
        binding.cbSexActive.isChecked = mouseEntity.sexActive == true
        binding.cbMc.isChecked = mouseEntity.MC == true

        binding.etMouseCodeUpdate.setText(mouseEntity.code.toString())
        binding.etWeight.setText(mouseEntity.weight.toString())
        binding.etTestesLength.setText(mouseEntity.testesLength.toString())
        binding.etTestesWidth.setText(mouseEntity.testesWidth.toString())
        binding.etMouseNote.setText(mouseEntity.note.toString())

        binding.etBody.setText(mouseEntity.body.toString())
        binding.etTail.setText(mouseEntity.tail.toString())
        binding.etFeet.setText(mouseEntity.feet.toString())
        binding.etEar.setText(mouseEntity.ear.toString())

        binding.etEmbryoRight.setText(mouseEntity.embryoRight.toString())
        binding.etEmbryoLeft.setText(mouseEntity.embryoLeft.toString())
        binding.etEmbryoDiameter.setText(mouseEntity.embryoDiameter.toString())
        binding.etMcRight.setText(mouseEntity.MCright.toString())
        binding.etMcLeft.setText(mouseEntity.MCleft.toString())

        when(mouseEntity.sex) {
            EnumSex.MALE.myName -> {
                binding.rbMale.isChecked = true
                hideNonMaleFields()
            }
            EnumSex.FEMALE.myName -> binding.rbFemale.isChecked = true

            null -> {
                binding.rbNullSex.isChecked = true
                hideNonMaleFields()
            }
        }

        when(mouseEntity.age) {
            EnumMouseAge.ADULT.myName -> binding.rbAdult.isChecked = true
            EnumMouseAge.SUBADULT.myName -> binding.rbSubadult.isChecked = true
            EnumMouseAge.JUVENILE.myName -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when(mouseEntity.captureID) {
            EnumCaptureID.CAPTURED.myName -> binding.rbCaptured.isChecked = true
            EnumCaptureID.DIED.myName -> binding.rbDied.isChecked = true
            EnumCaptureID.ESCAPED.myName -> binding.rbEscaped.isChecked = true
            EnumCaptureID.RELEASED.myName -> binding.rbReleased.isChecked = true
            null -> binding.rbNullCapture.isChecked = true
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
            protocolID = listProtocolEntity.firstOrNull {
                it.protocolName == name
            }?.protocolId
        }

        binding.rgSex.setOnCheckedChangeListener{ _, radioButtonId ->
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

        binding.rgAge.setOnCheckedChangeListener { _, radioButtonId ->
            when(radioButtonId){
                binding.rbAdult.id -> age = EnumMouseAge.ADULT.myName
                binding.rbJuvenile.id -> age = EnumMouseAge.JUVENILE.myName
                binding.rbSubadult.id -> age = EnumMouseAge.SUBADULT.myName
                binding.rbNullAge.id -> age = null
            }
        }

        binding.rgCaptureId.setOnCheckedChangeListener { _, radioButtonId ->
            when(radioButtonId){
                binding.rbCaptured.id -> captureID = EnumCaptureID.CAPTURED.myName
                binding.rbDied.id -> captureID = EnumCaptureID.DIED.myName
                binding.rbEscaped.id -> captureID = EnumCaptureID.ESCAPED.myName
                binding.rbReleased.id -> captureID = EnumCaptureID.RELEASED.myName
                binding.rbNullCapture.id -> captureID = null
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

    private fun goToCamera() {
        val action = UpdateMouseFragmentDirections.actionUpdateMouseFragmentToTakePhotoFragment("Mouse",
            currentMouseEntity.mouseIid, currentMouseEntity.deviceID)
        findNavController().navigate(action)
    }

    private fun deleteMouse(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){_, _ ->

            mouseListViewModel.deleteMouse(args.mouseOccTuple.mouseId, mouseImageEntity?.path)

            Toast.makeText(requireContext(),"Mouse deleted.", Toast.LENGTH_LONG).show()

            findNavController().navigateUp()
        }
            .setNegativeButton("No"){_, _ -> }
            .setTitle("Delete Mouse?")
            .setMessage("Are you sure you want to delete this mouse?")
            .create().show()
    }

    private fun updateMouse() {
        if (speciesID != null){
            val mouseEntity: MouseEntity = currentMouseEntity.copy()
            mouseEntity.speciesID = speciesID!!
            mouseEntity.trapID = giveOutPutInt(binding.autoCompTvTrapId.text.toString())
            mouseEntity.sex = sex
            mouseEntity.age = age
            mouseEntity.captureID = captureID
            mouseEntity.mouseDateTimeUpdated = Calendar.getInstance().time

            mouseEntity.protocolID = protocolID
            mouseEntity.sexActive = binding.cbSexActive.isChecked
            mouseEntity.weight = giveOutPutFloat(binding.etWeight.text.toString())

            mouseEntity.body = giveOutPutFloat(binding.etBody.text.toString())
            mouseEntity.tail = giveOutPutFloat(binding.etTail.text.toString())
            mouseEntity.feet = giveOutPutFloat(binding.etFeet.text.toString())
            mouseEntity.ear = giveOutPutFloat(binding.etEar.text.toString())

            mouseEntity.testesLength = giveOutPutFloat(binding.etTestesLength.text.toString())
            mouseEntity.testesWidth = giveOutPutFloat(binding.etTestesWidth.text.toString())

            mouseEntity.gravidity = binding.cbGravit.isChecked
            mouseEntity.lactating = binding.cbLactating.isChecked
            //počet embryí v oboch rohoch maternice a ich priemer
            mouseEntity.embryoRight = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            mouseEntity.embryoLeft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            mouseEntity.embryoDiameter = if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            mouseEntity.MC = binding.cbMc.isChecked
            //počet placentálnych polypov
            mouseEntity.MCright = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
            mouseEntity.MCleft = if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())
            mouseEntity.note = binding.etMouseNote.text.toString().ifBlank { null }

            // update mouse
            if (mouseEntity.weight != null) {
                checkWeightAndSave(mouseEntity)
            }
            else if (mouseEntity.trapID != null && mouseEntity.trapID in trapList) {
                checkTrapAvailability(mouseEntity)
            }
            else {
                executeTask(mouseEntity)
            }

        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkWeightAndSave(mouseEntity: MouseEntity) {
        if (mouseEntity.weight!! > specie?.maxWeight!! || mouseEntity.weight!! < specie?.minWeight!!){
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes"){_, _ ->
                checkTrapAvailability(mouseEntity)
            }
                .setNegativeButton("No"){_, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        }else{
            checkTrapAvailability(mouseEntity)
        }
    }

    private fun checkTrapAvailability(mouseEntity: MouseEntity) {
        if (mouseEntity.trapID != null && mouseEntity.trapID in trapList) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                executeTask(mouseEntity)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Trap In Use")
                .setMessage("Selected trap is in use, save anyway?")
                .create().show()
        } else {
            executeTask(mouseEntity)
        }
    }

    private fun executeTask(mouseEntity: MouseEntity) {
        mouseListViewModel.updateMouse(mouseEntity)

        Toast.makeText(requireContext(), "Mouse updated.", Toast.LENGTH_SHORT).show()

        findNavController().navigateUp()
    }

    private fun giveOutPutInt(input: String?): Int?{
        return if (input.isNullOrBlank() || input == "null") null else Integer.parseInt(input)
    }

    private fun giveOutPutFloat(input: String?): Float?{
        return if (input.isNullOrBlank() || input == "null") null else input.toFloat()
    }

    // recapture
    private fun fillDropDown(mouseEntity: MouseEntity) {
        specieListViewModel.getSpeciesForSelect().observe(viewLifecycleOwner) {
            listSpecie = it
            val listCode = mutableListOf<String>()
            it.forEach { specie ->
                listCode.add(specie.speciesCode)
            }
            val dropDownArrSpecie =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, listCode)
            binding.autoCompTvSpecie.setAdapter(dropDownArrSpecie)

            // init values
            specie = it.first { specie ->
                specie.specieId == mouseEntity.speciesID
            }
            speciesID = specie?.specieId
            binding.autoCompTvSpecie.setText(specie?.speciesCode, false)

        }

        protocolViewModel.procolList.observe(viewLifecycleOwner) {
            listProtocolEntity = it
            val listProtName = mutableListOf<String>()
            listProtName.add("null")
            it.forEach { protocol ->
                listProtName.add(protocol.protocolName)
            }
            val dropDownArrProtocol =
                ArrayAdapter(requireContext(), R.layout.dropdown_names, listProtName)
            binding.autoCompTvProtocol.setAdapter(dropDownArrProtocol)

            // init values
            val protocol = it.firstOrNull { protocol ->
                protocol.protocolId == mouseEntity.protocolID
            }
            protocolID = protocol?.protocolId
            binding.autoCompTvProtocol.setText(protocol.toString(), false)

        }

        val dropDownArrTrapID =
            ArrayAdapter(
                requireContext(),
                R.layout.dropdown_names,
                (1..args.occList.numTraps).toList()
            )
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

        binding.autoCompTvTrapId.setText(mouseEntity.trapID.toString(), false)
    }

    private fun initMouseValuesToView(mouseEntity: MouseEntity) {
        binding.cbGravit.isChecked = mouseEntity.gravidity == true
        binding.cbLactating.isChecked = mouseEntity.lactating == true
        binding.cbSexActive.isChecked = mouseEntity.sexActive == true
        binding.cbMc.isChecked = mouseEntity.MC == true

        binding.etWeight.setText(mouseEntity.weight.toString())
        binding.etTestesLength.setText(mouseEntity.testesLength.toString())
        binding.etTestesWidth.setText(mouseEntity.testesWidth.toString())
        binding.etMouseNote.setText(mouseEntity.note.toString())

        binding.etBody.setText(mouseEntity.body.toString())
        binding.etTail.setText(mouseEntity.tail.toString())
        binding.etFeet.setText(mouseEntity.feet.toString())
        binding.etEar.setText(mouseEntity.ear.toString())

        binding.etEmbryoRight.setText(mouseEntity.embryoRight.toString())
        binding.etEmbryoLeft.setText(mouseEntity.embryoLeft.toString())
        binding.etEmbryoDiameter.setText(mouseEntity.embryoDiameter.toString())
        binding.etMcRight.setText(mouseEntity.MCright.toString())
        binding.etMcLeft.setText(mouseEntity.MCleft.toString())

        when (mouseEntity.sex) {
            EnumSex.MALE.myName -> {
                binding.rbMale.isChecked = true
                hideNonMaleFields()
            }
            EnumSex.FEMALE.myName -> binding.rbFemale.isChecked = true

            null -> {
                binding.rbNullSex.isChecked = true
                hideNonMaleFields()
            }
        }

        when (mouseEntity.age) {
            EnumMouseAge.ADULT.myName -> binding.rbAdult.isChecked = true
            EnumMouseAge.SUBADULT.myName -> binding.rbSubadult.isChecked = true
            EnumMouseAge.JUVENILE.myName -> binding.rbJuvenile.isChecked = true
            null -> binding.rbNullAge.isChecked = true
        }

        when (mouseEntity.captureID) {
            EnumCaptureID.CAPTURED.myName -> binding.rbCaptured.isChecked = true
            EnumCaptureID.DIED.myName -> binding.rbDied.isChecked = true
            EnumCaptureID.ESCAPED.myName -> binding.rbEscaped.isChecked = true
            EnumCaptureID.RELEASED.myName -> binding.rbReleased.isChecked = true
            null -> binding.rbNullCapture.isChecked = true
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
            protocolID = listProtocolEntity.firstOrNull {
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

    private fun goToCamera() {
        if (mouseId <= 0) {
            Toast.makeText(requireContext(), "You need to insert a mouse first.", Toast.LENGTH_LONG)
                .show()
            return
        }

        val action = RecaptureMouseFragmentDirections.actionRecaptureMouseFragmentToTakePhotoFragment(
            "Mouse",
            mouseId,
            currentMouseEntity.deviceID
        )
        findNavController().navigate(action)
    }

    private fun recaptureMouse() {
        if (mouseId > 0) {
            Toast.makeText(requireContext(), "Mouse already inserted.", Toast.LENGTH_LONG).show()
            return
        }

        if (speciesID != null) {
            val mouseEntity: MouseEntity = currentMouseEntity.copy()
            mouseEntity.mouseId = 0
            mouseEntity.mouseIid = 0
            mouseEntity.primeMouseID =
                if (args.recapMouse.primeMouseID == null) currentMouseEntity.mouseIid else args.recapMouse.primeMouseID
            mouseEntity.occasionID = args.occList.occasionId
            mouseEntity.localityID = args.occList.localityID
            mouseEntity.mouseDateTimeCreated = Calendar.getInstance().time
            mouseEntity.mouseDateTimeUpdated = null
            mouseEntity.recapture = true
            mouseEntity.speciesID = speciesID!!
            mouseEntity.trapID = giveOutPutInt(binding.autoCompTvTrapId.text.toString())
            mouseEntity.sex = sex
            mouseEntity.age = age
            mouseEntity.captureID = captureID

            mouseEntity.protocolID = protocolID
            mouseEntity.sexActive = binding.cbSexActive.isChecked
            mouseEntity.weight = giveOutPutFloat(binding.etWeight.text.toString())
            mouseEntity.testesLength = giveOutPutFloat(binding.etTestesLength.text.toString())
            mouseEntity.testesWidth = giveOutPutFloat(binding.etTestesWidth.text.toString())

            mouseEntity.body = giveOutPutFloat(binding.etBody.text.toString())
            mouseEntity.tail = giveOutPutFloat(binding.etTail.text.toString())
            mouseEntity.feet = giveOutPutFloat(binding.etFeet.text.toString())
            mouseEntity.ear = giveOutPutFloat(binding.etEar.text.toString())

            mouseEntity.gravidity = binding.cbGravit.isChecked
            mouseEntity.lactating = binding.cbLactating.isChecked
            //počet embryí v oboch rohoch maternice a ich priemer
            mouseEntity.embryoRight =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoRight.text.toString())
            mouseEntity.embryoLeft =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etEmbryoLeft.text.toString())
            mouseEntity.embryoDiameter =
                if (sex == EnumSex.MALE.myName) null else giveOutPutFloat(binding.etEmbryoDiameter.text.toString())
            mouseEntity.MC = binding.cbMc.isChecked
            //počet placentálnych polypov
            mouseEntity.MCright =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcRight.text.toString())
            mouseEntity.MCleft =
                if (sex == EnumSex.MALE.myName) null else giveOutPutInt(binding.etMcLeft.text.toString())

            mouseEntity.note = binding.etMouseNote.text.toString().ifBlank { null }

            mouseEntity.mouseCaught = Calendar.getInstance().time.time

            // recapture
            if (mouseEntity.weight != null && specie?.minWeight != null && specie?.maxWeight != null) {
                checkWeightAndSave(mouseEntity)
            }
            else if (mouseEntity.trapID != null && mouseEntity.trapID in trapList) {
                checkTrapAvailability(mouseEntity)
            }
            else {
                executeTask(mouseEntity)
            }

        } else {
            Toast.makeText(requireContext(), getString(R.string.emptyFields), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun checkWeightAndSave(mouseEntity: MouseEntity) {
        if (mouseEntity.weight!! > specie!!.maxWeight!! || mouseEntity.weight!! < specie!!.minWeight!!) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                checkTrapAvailability(mouseEntity)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Mouse Weight")
                .setMessage("Mouse weight out of bounds, save anyway?")
                .create().show()
        } else {
            checkTrapAvailability(mouseEntity)
        }
    }

    private fun checkTrapAvailability(mouseEntity: MouseEntity) {
        if (mouseEntity.trapID != null && mouseEntity.trapID in trapList) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                executeTask(mouseEntity)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Trap In Use")
                .setMessage("Selected trap is in use, save anyway?")
                .create().show()
        } else {
            executeTask(mouseEntity)
        }
    }

    private fun executeTask(mouseEntity: MouseEntity) {
        mouseListViewModel.insertMouse(mouseEntity)

        Toast.makeText(requireContext(), "Mouse recaptured.", Toast.LENGTH_SHORT).show()

        mouseListViewModel.mouseId.observe(viewLifecycleOwner) {
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