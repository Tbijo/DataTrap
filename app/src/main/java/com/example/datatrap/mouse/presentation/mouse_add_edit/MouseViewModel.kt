package com.example.datatrap.mouse.presentation.mouse_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.core.data.storage.InternalStorageRepository
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
import com.example.datatrap.mouse.domain.use_case.InsertMouseUseCase
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.settings.data.protocol.ProtocolEntity
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
    private val mouseImageRepository: MouseImageRepository,
    private val internalStorageRepository: InternalStorageRepository,
    private val protocolRepository: ProtocolRepository,
    private val specieRepository: SpecieRepository,
    private val occasionRepository: OccasionRepository,
    private val getOccupiedTrapIdsInOccasion: GetOccupiedTrapIdsInOccasion,
    private val generateCodeUseCase: GenerateCodeUseCase,
    private val insertMouseUseCase: InsertMouseUseCase,
): ViewModel() {

    private var occasionId: String? = null
    private var localityId: String? = null
    private var isRecapture: Boolean? = null

    private val _state = MutableStateFlow(MouseUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val mouseId = savedStateHandle.getMainScreenNavArgs()?.mouseId
            occasionId = savedStateHandle.getMainScreenNavArgs()?.occasionId
            localityId = savedStateHandle.getMainScreenNavArgs()?.localityId
            isRecapture = savedStateHandle.getMainScreenNavArgs()?.isRecapture

            protocolRepository.getSettingsEntityList().collect { proList ->
                _state.update { it.copy(
                    protocolList = proList.filterIsInstance<ProtocolEntity>(),
                ) }
            }

            specieRepository.getSpecies().collect { species ->
                _state.update { it.copy(
                    specieList = species,
                ) }
            }

            mouseId?.let {
                val mouse = mouseRepository.getMouse(mouseId)
                val image = mouseImageRepository.getImageForMouse(mouseId)

                _state.update { it.copy(
                    mouseEntity = mouse,
                    imageId = image?.mouseImgId,
                    imageName = image?.imgName,
                    imageNote = image?.note,
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
                _state.update { it.copy(
                    imageName = event.imageName,
                    imageNote = event.imageNote,
                ) }
            }
            is MouseScreenEvent.OnLeave -> {
                deleteImageOnLeave()
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
        with(state.value) {
            val specie = specieEntity
            val code = code.toIntOrNull()

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
                isSheetExpanded = !isSheetExpanded,
            ) }
        }
    }

    // TODO if lengths or weight not good, show all in one dialog then user decides whether is okay or change
    // Continue and Cancel buttons in Dialog Window
    private fun insertMouse() {
        with(state.value) {
            val specie = specieEntity
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

                val weight: Float? = weight.toFloatOrNull()
                val minWeightOfSpecie = specieEntity?.minWeight
                val maxWeightOfSpecie = specieEntity?.maxWeight
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

                val captureID = captureID?.myName

                val sexActive: Boolean = sexActive

                val body = body.toFloatOrNull()
                val tail = tail.toFloatOrNull()
                val feet = feet.toFloatOrNull()
                val ear = ear.toFloatOrNull()

                val sex = sex?.myName
                val age = state.value.age?.myName

                val testesLength: Float? = testesLength.toFloatOrNull()
                val testesWidth: Float? = testesWidth.toFloatOrNull()

                val gravidity = gravidity
                val lactating = lactating

                // počet embryí v oboch rohoch maternice a ich priemer
                val embryoRight = rightEmbryo.toIntOrNull()
                val embryoLeft = leftEmbryo.toIntOrNull()
                val embryoDiameter = embryoDiameter.toFloatOrNull()

                // počet placentálnych polypov
                val MC = mc
                val MCright = mcRight.toIntOrNull()
                val MCleft = mcLeft.toIntOrNull()

                val note: String? = note.ifEmpty { null }

                val currentMouse = mouseEntity

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

                insertMouseUseCase(occasionID, mouseEntity, imageName, imageNote)
                _eventFlow.emit(UiEvent.NavigateBack)
            }
        }
    }

    private fun initMouseValuesToView(mouseEntity: MouseEntity) {
        with(mouseEntity) {
            _state.update { it.copy(
                gravidity = gravidity == true,
                lactating = lactating == true,
                sexActive = sexActive == true,
                mc = MC == true,
                code = "$code",
                weight = "$weight",
                testesLength = "$testesLength",
                testesWidth = "$testesWidth",
                note = "$note",
                body = "$body",
                tail = "$tail",
                feet = "$feet",
                ear = "$ear",
                rightEmbryo = "$embryoRight",
                leftEmbryo = "$embryoLeft",
                embryoDiameter = "$embryoDiameter",
                mcRight = "$MCright",
                mcLeft = "$MCleft",
                sex = toEnumSex(sex),
                age = toEnumMouseAge(age),
                captureID = toEnumCaptureID(captureID),
            ) }
        }
    }

    private fun deleteImageOnLeave() {
        // if user leaves, physical image should be deleted if it is not saved in DB
        viewModelScope.launch(Dispatchers.IO) {
            if (state.value.imageId == null) {
                state.value.imageName?.let {
                    internalStorageRepository.deleteImage(it)
                }
            }
        }
    }

}