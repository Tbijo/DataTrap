package com.example.datatrap.mouse.presentation.mouse_add_edit

import android.app.AlertDialog
import android.widget.ArrayAdapter
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.R
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.core.data.pref.PrefRepository
import com.example.datatrap.core.util.EnumCaptureID
import com.example.datatrap.core.util.EnumMouseAge
import com.example.datatrap.core.util.EnumSex
import com.example.datatrap.core.util.Resource
import com.example.datatrap.core.util.ifNullOrBlank
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.use_case.GenerateCodeUseCase
import com.example.datatrap.mouse.domain.use_case.GetOccupiedTrapIdsInOccasion
import com.example.datatrap.mouse.navigation.MouseScreens
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.settings.protocol.data.ProtocolRepository
import com.example.datatrap.specie.data.SpecieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class MouseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mouseRepository: MouseRepository,
    private val getOccupiedTrapIdsInOccasion: GetOccupiedTrapIdsInOccasion = GetOccupiedTrapIdsInOccasion(mouseRepository),
    private val protocolRepository: ProtocolRepository,
    private val mouseImageRepository: MouseImageRepository,
    private val specieRepository: SpecieRepository,
    private val prefRepository: PrefRepository,
    private val generateCodeUseCase: GenerateCodeUseCase = GenerateCodeUseCase(mouseRepository, prefRepository),
    private val occasionRepository: OccasionRepository,
): ViewModel() {

    private var occasionId: String? = null
    private var localityId: String? = null

    private val _state = MutableStateFlow(MouseUiState())
    val state = _state.asStateFlow()

    //private val mouseImageEntity: MouseImageEntity? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val mouseId = savedStateHandle.get<String>(MouseScreens.MouseScreen.mouseIdKey)
            occasionId = savedStateHandle.get<String>(MouseScreens.MouseScreen.occasionIdKey)
            localityId = savedStateHandle.get<String>(MouseScreens.MouseScreen.localityIdKey)

            protocolRepository.getProtocolEntityList().collect { proList ->
                _state.update { it.copy(
                    protocolList = proList,
                ) }
            }

            viewModelScope.launch {
                _state.update { it.copy(
                    specieList = specieRepository.getSpecies(),
                ) }
            }

            mouseId?.let {
                val mouse = mouseRepository.getMouse(mouseId)
                _state.update { it.copy(
                    mouseEntity = mouse,
                ) }
            }

            occasionId?.let { occId ->
                // get occupied traps
                getOccupiedTrapIdsInOccasion(occId).collect { trapList ->
                    _state.update { it.copy(
                        occupiedTrapIdList = trapList,
                    ) }
                }

                // get max number of traps in occasion
                val numberOfTrapsInOccasion = occasionRepository.getOccasion(occId).numTraps
                _state.update { it.copy(
                    trapIDList = (1..numberOfTrapsInOccasion).toList()
                ) }
            }
        }
    }

    fun onEvent(event: MouseScreenEvent) {
        when(event) {
            MouseScreenEvent.OnInsertClick -> insertMouse()
            MouseScreenEvent.OnGenerateButtonClick -> generateCode()
            MouseScreenEvent.OnMouseClick -> showDrawnRat()
            is MouseScreenEvent.OnCaptureIdClick -> {
                _state.update { it.copy(
                    captureID = event.captureID
                ) }
            }
            MouseScreenEvent.OnCameraClick -> TODO()
            MouseScreenEvent.OnProtocolDropDownClick -> TODO()
            MouseScreenEvent.OnProtocolDropDownDismiss -> TODO()
            is MouseScreenEvent.OnSelectProtocol -> TODO()
            is MouseScreenEvent.OnSelectSpecie -> TODO()
            is MouseScreenEvent.OnSelectTrapID -> TODO()
            MouseScreenEvent.OnSpecieDropDownClick -> TODO()
            MouseScreenEvent.OnSpecieDropDownDismiss -> TODO()
            MouseScreenEvent.OnTrapIDDropDownClick -> TODO()
            MouseScreenEvent.OnTrapIDDropDownDismiss -> TODO()
            is MouseScreenEvent.OnAgeClick -> TODO()
            is MouseScreenEvent.OnBodyTextChanged -> TODO()
            is MouseScreenEvent.OnCodeTextChanged -> TODO()
            is MouseScreenEvent.OnEarTextChanged -> TODO()
            is MouseScreenEvent.OnEmbryoDiameterTextChanged -> TODO()
            is MouseScreenEvent.OnFeetTextChanged -> TODO()
            MouseScreenEvent.OnGravidityClick -> TODO()
            MouseScreenEvent.OnLactatingClick -> TODO()
            is MouseScreenEvent.OnLeftEmbryoTextChanged -> TODO()
            MouseScreenEvent.OnMcClick -> TODO()
            is MouseScreenEvent.OnMcLeftTextChanged -> TODO()
            is MouseScreenEvent.OnMcRightTextChanged -> TODO()
            is MouseScreenEvent.OnNoteTextChanged -> TODO()
            is MouseScreenEvent.OnRightEmbryoTextChanged -> TODO()
            MouseScreenEvent.OnSexActiveClick -> TODO()
            is MouseScreenEvent.OnSexClick -> TODO()
            is MouseScreenEvent.OnTailTextChanged -> TODO()
            is MouseScreenEvent.OnTestesLengthTextChanged -> TODO()
            is MouseScreenEvent.OnTestesWidthTextChanged -> TODO()
            is MouseScreenEvent.OnWeightTextChanged -> TODO()
        }
    }

    private fun generateCode() {
        viewModelScope.launch(Dispatchers.Default) {
            generateCodeUseCase(
                upperFingers = state.value.specieEntity?.upperFingers,
                specieCode = state.value.specieEntity?.speciesCode,
                captureID = state.value.captureID?.myName,
                localityId = localityId,
            ).collect { result ->
                when(result) {
                    is Resource.Error -> {
                        _state.update { it.copy(
                            error = result.throwable?.message
                        ) }
                    }
                    is Resource.Success -> {
                        result.data?.let { code ->
                            _state.update { it.copy(
                                code = "$code"
                            ) }
                        }
                    }
                }
            }
        }
    }
    private fun showDrawnRat() {
        if (state.value.specieEntity == null) {
            _state.update { it.copy(
                error = "Select a Specie",
            ) }
            return
        }
        if (speciesID <= 0 || code == null || code <= 0 || state.value.code.length >= 5) {
            _state.update { it.copy(
                error = "Generate a valid code.",
            ) }
            return
        }
        // TODO Show MouseSketch as Roll-Up Window
        // pass in code!!, specie?.upperFingers!!
    }

    private fun insertMouse() {
        val specieID = state.value.specieEntity?.specieId
        if (specieID == null) {
            _state.update { it.copy(
                error = "Fill required fields.",
            ) }
            return
        }

        val occasionID: String = occasionId.ifNullOrBlank {
            _state.update { it.copy(
                error = "This should not happen with occasionID."
            ) }
            return
        }!!

        val localityID: String = localityId.ifNullOrBlank {
            _state.update { it.copy(
                error = "This should not happen with localityID."
            ) }
            return
        }!!

        val code: Int = Integer.parseInt(state.value.code)
        if (code in activeCodeList) {
            _state.update { it.copy(
                error = "Code is not available.",
            ) }
            return
        }

        val weight: Float? = state.value.weight.toFloatOrNull()
        val minWeightOfSpecie = state.value.specieEntity?.minWeight
        val maxWeightOfSpecie = state.value.specieEntity?.maxWeight
        if (weight != null && minWeightOfSpecie != null && maxWeightOfSpecie != null) {
            // TODO how to make function wait for modal Window?
            if (mouseEntity.weight!! > specie?.maxWeight!! || mouseEntity.weight!! < specie?.minWeight!!) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setPositiveButton("Yes") { _, _ ->
                    checkTrapAvailability(mouseEntity)
                }
                    .setNegativeButton("No") { _, _ -> }
                    .setTitle("Warning: Mouse Weight")
                    .setMessage("Mouse weight out of bounds, save anyway?")
                    .create().show()
            }
        }

        val trapID: Int? = state.value.trapID
        if (trapID != null && trapID in state.value.occupiedTrapIdList) {
            // TODO how to make function wait for modal Window?
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                executeTask(mouseEntity)
            }
                .setNegativeButton("No") { _, _ -> }
                .setTitle("Warning: Trap In Use")
                .setMessage("Selected trap is in use, save anyway?")
                .create().show()
        }

        val captureID = state.value.captureID?.myName

        val sexActive: Boolean = state.value.sexActive

        val body = state.value.body.toFloatOrNull()
        val tail = state.value.tail.toFloatOrNull()
        val feet = state.value.feet.toFloatOrNull()
        val ear = state.value.ear.toFloatOrNull()

        val sex = state.value.sex?.myName
        val age = state.value.age?.myName

        val testesLength: Float? = state.value.testesLength.toFloatOrNull()
        val testesWidth: Float? = state.value.testesWidth.toFloatOrNull()

        val gravidity = state.value.gravidity
        val lactating = state.value.lactating

        // počet embryí v oboch rohoch maternice a ich priemer
        val embryoRight = state.value.rightEmbryo.toIntOrNull()
        val embryoLeft = state.value.leftEmbryo.toIntOrNull()
        val embryoDiameter = state.value.embryoDiameter.toFloatOrNull()

        // počet placentálnych polypov
        val MC = state.value.mc
        val MCright = state.value.mcRight.toIntOrNull()
        val MCleft = state.value.mcLeft.toIntOrNull()

        val note: String? = state.value.note.ifEmpty { null }

        val currentMouse = state.value.mouseEntity

        val mouseEntity: MouseEntity = if (currentMouse == null) {
            MouseEntity(
                code = code,
                primeMouseID = null,
                speciesID = specieID,
                protocolID = state.value.protocolEntity?.protocolId,
                occasionID = occasionID,
                localityID = localityID,
                trapID = trapID,
                mouseDateTimeCreated = ZonedDateTime.now(),
                mouseDateTimeUpdated = null,
                sex = sex,
                age = age,
                gravidity = gravidity,
                lactating = lactating,
                sexActive = sexActive,
                weight = weight,
                recapture = false,
                captureID = captureID,
                body = body,
                tail = tail,
                feet = feet,
                ear = ear,
                testesLength = testesLength,
                testesWidth = testesWidth,
                embryoRight = embryoRight,
                embryoLeft = embryoLeft,
                embryoDiameter = embryoDiameter,
                MC = MC,
                MCright = MCright,
                MCleft = MCleft,
                note = note,
                mouseCaught = ZonedDateTime.now(),
            )
        } else {
            MouseEntity(
                mouseId = currentMouse.mouseId,
                code = code,
                primeMouseID = currentMouse.primeMouseID,
                speciesID = specieID,
                protocolID = state.value.protocolEntity?.protocolId,
                occasionID = currentMouse.occasionID,
                localityID = currentMouse.localityID,
                trapID = trapID,
                mouseDateTimeCreated = currentMouse.mouseDateTimeCreated,
                mouseDateTimeUpdated = ZonedDateTime.now(),
                sex = sex,
                age = age,
                gravidity = gravidity,
                lactating = lactating,
                sexActive = sexActive,
                weight = weight,
                recapture = currentMouse.recapture,
                captureID = captureID,
                body = body,
                tail = tail,
                feet = feet,
                ear = ear,
                testesLength = testesLength,
                testesWidth = testesWidth,
                embryoRight = embryoRight,
                embryoLeft = embryoLeft,
                embryoDiameter = embryoDiameter,
                MC = MC,
                MCright = MCright,
                MCleft = MCleft,
                note = note,
                mouseCaught = currentMouse.mouseCaught,
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            mouseRepository.insertMouse(mouseEntity)
        }
    }

    private fun fillDropDown(mouseEntity: MouseEntity) {
        mouseListViewModel.getMouse(args.mouseOccTuple.mouseId).observe(viewLifecycleOwner) {
            fillDropDown(it)
        }
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

        val dropDownArrTrapID = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_names,
            (1..args.occList.numTraps).toList()
        )
        binding.autoCompTvTrapId.setAdapter(dropDownArrTrapID)

        // init value
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
    private fun hideFemaleFields() {
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
    private fun showFemaleFields() {
        binding.cbGravit.isEnabled = true
        binding.cbLactating.isEnabled = true
        binding.cbMc.isEnabled = true
        binding.etMcRight.isEnabled = true
        binding.etMcLeft.isEnabled = true
        binding.etEmbryoRight.isEnabled = true
        binding.etEmbryoLeft.isEnabled = true
        binding.etEmbryoDiameter.isEnabled = true
    }

}