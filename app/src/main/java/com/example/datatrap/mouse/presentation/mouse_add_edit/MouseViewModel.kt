package com.example.datatrap.mouse.presentation.mouse_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.core.getMainScreenNavArgs
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.util.Resource
import com.example.datatrap.core.util.ifNullOrBlank
import com.example.datatrap.core.util.toEnumCaptureID
import com.example.datatrap.core.util.toEnumMouseAge
import com.example.datatrap.core.util.toEnumSex
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.use_case.GenerateCodeUseCase
import com.example.datatrap.mouse.domain.use_case.GetOccupiedTrapIdsInOccasion
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.settings.data.protocol.ProtocolRepository
import com.example.datatrap.specie.data.SpecieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MouseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mouseRepository: MouseRepository,
    private val getOccupiedTrapIdsInOccasion: GetOccupiedTrapIdsInOccasion,
    private val protocolRepository: ProtocolRepository,
    private val specieRepository: SpecieRepository,
    private val generateCodeUseCase: GenerateCodeUseCase,
    private val occasionRepository: OccasionRepository,
): ViewModel() {

    private var occasionId: String? = null
    private var localityId: String? = null
    private var isRecapture: Boolean? = null

    private val _state = MutableStateFlow(MouseUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    //private val mouseImageEntity: MouseImageEntity? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val mouseId = savedStateHandle.getMainScreenNavArgs()?.mouseId
            occasionId = savedStateHandle.getMainScreenNavArgs()?.occasionId
            localityId = savedStateHandle.getMainScreenNavArgs()?.localityId
            isRecapture = savedStateHandle.getMainScreenNavArgs()?.isRecapture

            protocolRepository.getProtocolEntityList().collect { proList ->
                _state.update { it.copy(
                    protocolList = proList,
                ) }
            }

            _state.update { it.copy(
                specieList = specieRepository.getSpecies(),
            ) }

            mouseId?.let {
                val mouse = mouseRepository.getMouse(mouseId)

                _state.update { it.copy(
                    mouseEntity = mouse,
                ) }
                initMouseValuesToView(mouse)
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
                    trapIDList = (1..numberOfTrapsInOccasion).toList(),
                ) }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: MouseScreenEvent) {
        when(event) {
            MouseScreenEvent.OnInsertClick -> insertMouse()

            MouseScreenEvent.OnGenerateButtonClick -> generateCode()

            MouseScreenEvent.OnMouseClick -> showDrawnRat()

            is MouseScreenEvent.OnCaptureIdClick -> {
                _state.update { it.copy(
                    captureID = event.captureID,
                ) }
            }
            is MouseScreenEvent.OnCodeTextChanged -> {
                _state.update { it.copy(
                    code = event.text,
                    codeError = null,
                ) }
            }
            is MouseScreenEvent.OnBodyTextChanged -> {
                _state.update { it.copy(
                    body = event.text,
                    bodyError = null,
                ) }
            }
            is MouseScreenEvent.OnTailTextChanged -> {
                _state.update { it.copy(
                    tail = event.text,
                    tailError = null,
                ) }
            }
            is MouseScreenEvent.OnEarTextChanged -> {
                _state.update { it.copy(
                    ear = event.text,
                    earError = null,
                ) }
            }
            is MouseScreenEvent.OnFeetTextChanged -> {
                _state.update { it.copy(
                    feet = event.text,
                    feetError = null,
                ) }
            }
            is MouseScreenEvent.OnRightEmbryoTextChanged -> {
                _state.update { it.copy(
                    rightEmbryo = event.text,
                    rightEmbryoError = null,
                ) }
            }
            is MouseScreenEvent.OnLeftEmbryoTextChanged -> {
                _state.update { it.copy(
                    leftEmbryo = event.text,
                    leftEmbryoError = null,
                ) }
            }
            is MouseScreenEvent.OnEmbryoDiameterTextChanged -> {
                _state.update { it.copy(
                    embryoDiameter = event.text,
                    embryoDiameterError = null,
                ) }
            }
            is MouseScreenEvent.OnMcLeftTextChanged -> {
                _state.update { it.copy(
                    mcLeft = event.text,
                    mcLeftError = null,
                ) }
            }
            is MouseScreenEvent.OnMcRightTextChanged -> {
                _state.update { it.copy(
                    mcRight = event.text,
                    mcRightError = null,
                ) }
            }
            is MouseScreenEvent.OnNoteTextChanged -> {
                _state.update { it.copy(
                    note = event.text,
                    noteError = null,
                ) }
            }
            is MouseScreenEvent.OnTestesLengthTextChanged -> {
                _state.update { it.copy(
                    testesLength = event.text,
                    testesLengthError = null,
                ) }
            }
            is MouseScreenEvent.OnTestesWidthTextChanged -> {
                _state.update { it.copy(
                    testesWidth = event.text,
                    testesWidthError = null,
                ) }
            }
            is MouseScreenEvent.OnWeightTextChanged -> {
                _state.update { it.copy(
                    weight = event.text,
                    weightError = null,
                ) }
            }
            MouseScreenEvent.OnProtocolDropDownClick -> {
                _state.update { it.copy(
                    isProtocolExpanded = !state.value.isProtocolExpanded,
                ) }
            }
            MouseScreenEvent.OnProtocolDropDownDismiss -> {
                _state.update { it.copy(
                    isProtocolExpanded = false,
                ) }
            }
            is MouseScreenEvent.OnSelectProtocol -> {
                _state.update { it.copy(
                    protocolEntity = event.protocol,
                ) }
            }
            MouseScreenEvent.OnSpecieDropDownClick -> {
                _state.update { it.copy(
                    isSpecieExpanded = !state.value.isSpecieExpanded,
                ) }
            }
            MouseScreenEvent.OnSpecieDropDownDismiss -> {
                _state.update { it.copy(
                    isSpecieExpanded = false,
                ) }
            }
            is MouseScreenEvent.OnSelectSpecie -> {
                _state.update { it.copy(
                    specieEntity = event.specieEntity,
                ) }
            }
            MouseScreenEvent.OnTrapIDDropDownClick -> {
                _state.update { it.copy(
                    isTrapIDExpanded = !state.value.isTrapIDExpanded,
                ) }
            }
            MouseScreenEvent.OnTrapIDDropDownDismiss -> {
                _state.update { it.copy(
                    isTrapIDExpanded = false,
                ) }
            }
            is MouseScreenEvent.OnSelectTrapID -> {
                _state.update { it.copy(
                    trapID = event.trapID,
                ) }
            }
            MouseScreenEvent.OnGravidityClick -> {
                _state.update { it.copy(
                    gravidity = !state.value.gravidity,
                ) }
            }
            MouseScreenEvent.OnLactatingClick -> {
                _state.update { it.copy(
                    lactating = !state.value.lactating,
                ) }
            }
            MouseScreenEvent.OnSexActiveClick -> {
                _state.update { it.copy(
                    sexActive = !state.value.sexActive,
                ) }
            }
            is MouseScreenEvent.OnAgeClick -> {
                _state.update { it.copy(
                    age = event.age,
                ) }
            }
            MouseScreenEvent.OnMcClick -> {
                _state.update { it.copy(
                    mc = !state.value.mc,
                ) }
            }
            is MouseScreenEvent.OnSexClick -> {
                _state.update { it.copy(
                    sex = event.sex,
                ) }
            }
            MouseScreenEvent.OnDialogCancelClick -> {
                _state.update { it.copy(
                    isDialogShowing = false,
                    isMouseOkay = false,
                ) }
            }
            MouseScreenEvent.OnDialogDismiss -> {
                _state.update { it.copy(
                    isDialogShowing = false,
                    isMouseOkay = false,
                ) }
            }
            MouseScreenEvent.OnDialogOkClick -> {
                _state.update { it.copy(
                    isDialogShowing = false,
                    isMouseOkay = true,
                ) }
            }
            is MouseScreenEvent.OnReceiveImageName -> {
                setImageId(
                    imageName = event.imageName,
                    imageNote = event.imageNote,
                )
            }

            else -> Unit
        }
    }

    private fun generateCode() {
        viewModelScope.launch(Dispatchers.IO) {
            generateCodeUseCase(
                upperFingers = state.value.specieEntity?.upperFingers,
                specieCode = state.value.specieEntity?.speciesCode,
                captureID = state.value.captureID?.myName,
                localityId = localityId,
            ).collect { result ->
                when(result) {
                    is Resource.Error -> {
                        _state.update { it.copy(
                            error = result.throwable?.message,
                        ) }
                    }
                    is Resource.Success -> {
                        result.data?.let { code ->
                            _state.update { it.copy(
                                code = "$code",
                            ) }
                        }
                    }
                }
            }
        }
    }

    private fun showDrawnRat() {
        val specie = state.value.specieEntity
        val code = state.value.code.toIntOrNull()

        if (specie == null) {
            _state.update { it.copy(
                error = "Select a Specie",
            ) }
            return
        }
        if (specie.upperFingers == null) {
            _state.update { it.copy(
                error = "Selected specie does not have upper fingers specified.",
            ) }
            return
        }
        if (code == null || code <= 0 || code.toString().length >= 5) {
            _state.update { it.copy(
                error = "Generate a valid code.",
            ) }
            return
        }
        _state.update { it.copy(
            isSheetExpanded = !state.value.isSheetExpanded,
        ) }
    }

    private fun insertMouse() {
        val specie = state.value.specieEntity
        val specieID = specie?.specieId

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

        viewModelScope.launch(Dispatchers.IO) {

            val code = state.value.code.toIntOrNull()

            val weight: Float? = state.value.weight.toFloatOrNull()
            val minWeightOfSpecie = state.value.specieEntity?.minWeight
            val maxWeightOfSpecie = state.value.specieEntity?.maxWeight
            if (weight != null && minWeightOfSpecie != null && maxWeightOfSpecie != null) {
                if (weight > maxWeightOfSpecie || weight < minWeightOfSpecie) {
                    // TODO other way to make function wait for modal Window?
                    _state.update { it.copy(
                        dialogTitle = "Warning: Mouse Weight",
                        dialogMessage = "Mouse weight out of bounds, save anyway?",
                        isDialogShowing = true,
                    ) }
                    // wait fo user to interact with dialog
                    // possible ensureActive?
                    launch { while (state.value.isDialogShowing) {} }.join()

                } else {
                    _state.update { it.copy(
                        isMouseOkay = true,
                    ) }
                }
            } else {
                _state.update { it.copy(
                    isMouseOkay = true,
                ) }
            }

            if (!state.value.isMouseOkay) {
                return@launch
            }

            val trapID: Int? = state.value.trapID
            if (trapID != null && trapID in state.value.occupiedTrapIdList) {
                // TODO other way to make function wait for modal Window?
                _state.update { it.copy(
                    dialogTitle = "Warning: Trap In Use",
                    dialogMessage = "Selected trap is in use, save anyway?",
                    isDialogShowing = true,
                ) }
                // wait fo user to interact with dialog
                // possible ensureActive?
                launch { while (state.value.isDialogShowing) {} }.join()

            } else {
                _state.update { it.copy(
                    isMouseOkay = true,
                ) }
            }

            if (!state.value.isMouseOkay) {
                return@launch
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
                if (isRecapture == true) {
                    MouseEntity(
                        mouseId = UUID.randomUUID().toString(),
                        code = code,
                        primeMouseID = currentMouse.primeMouseID ?: currentMouse.mouseId,
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
                        recapture = true,
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
            }

            mouseRepository.insertMouse(mouseEntity)
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }

    private fun initMouseValuesToView(mouseEntity: MouseEntity) {
        _state.update { it.copy(
            gravidity = mouseEntity.gravidity == true,
            lactating = mouseEntity.lactating == true,
            sexActive = mouseEntity.sexActive == true,
            mc = mouseEntity.MC == true,
            code = "${mouseEntity.code}",
            weight = "${mouseEntity.weight}",
            testesLength = "${mouseEntity.testesLength}",
            testesWidth = "${mouseEntity.testesWidth}",
            note = "${mouseEntity.note}",
            body = "${mouseEntity.body}",
            tail = "${mouseEntity.tail}",
            feet = "${mouseEntity.feet}",
            ear = "${mouseEntity.ear}",
            rightEmbryo = "${mouseEntity.embryoRight}",
            leftEmbryo = "${mouseEntity.embryoLeft}",
            embryoDiameter = "${mouseEntity.embryoDiameter}",
            mcRight = "${mouseEntity.MCright}",
            mcLeft = "${mouseEntity.MCleft}",
            sex = toEnumSex(mouseEntity.sex),
            age = toEnumMouseAge(mouseEntity.age),
            captureID = toEnumCaptureID(mouseEntity.captureID),
        ) }
    }

    private fun setImageId(
        imageName: String?,
        imageNote: String?,
    ) {
        // TODO create imageId in state class
        _state.update { it.copy(

        ) }
    }

}