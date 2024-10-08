package com.example.datatrap.occasion.presentation.occasion_add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.data.shared_nav_args.ScreenNavArgs
import com.example.datatrap.core.data.storage.InternalStorageRepository
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.util.ifNullOrBlank
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.domain.use_case.GetWeatherUseCase
import com.example.datatrap.occasion.domain.use_case.InsertOccasionUseCase
import com.example.datatrap.settings.data.env_type.EnvTypeEntity
import com.example.datatrap.settings.data.env_type.EnvTypeRepository
import com.example.datatrap.settings.data.method.MethodEntity
import com.example.datatrap.settings.data.method.MethodRepository
import com.example.datatrap.settings.data.methodtype.MethodTypeEntity
import com.example.datatrap.settings.data.methodtype.MethodTypeRepository
import com.example.datatrap.settings.data.traptype.TrapTypeEntity
import com.example.datatrap.settings.data.traptype.TrapTypeRepository
import com.example.datatrap.settings.data.veg_type.VegetTypeEntity
import com.example.datatrap.settings.data.veg_type.VegetTypeRepository
import com.example.datatrap.settings.user.data.UserRepository
import com.example.datatrap.sync.utils.onError
import com.example.datatrap.sync.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

class OccasionViewModel(
    private val occasionRepository: OccasionRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val internalStorageRepository: InternalStorageRepository,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val localityRepository: LocalityRepository,
    private val envTypeRepository: EnvTypeRepository,
    private val methodRepository: MethodRepository,
    private val methodTypeRepository: MethodTypeRepository,
    private val trapTypeRepository: TrapTypeRepository,
    private val vegetTypeRepository: VegetTypeRepository,
    private val userRepository: UserRepository,
    private val screenNavArgs: ScreenNavArgs,
    private val insertOccasionUseCase: InsertOccasionUseCase,
    private val occasionId: String?,
    private val localityId: String?,
    private val sessionId: String?,
): ViewModel() {

    // TODO remove non UI state var from state class, some vars do not change UI
    private val _state = MutableStateFlow(OccasionUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // number of occasions in a session to calculate occasionNum of a new OccasionEntity
    private var occasionCount: Int = 0

    init {
        viewModelScope.launch(Dispatchers.IO) {
            screenNavArgs.readUserId()?.let { userId ->
                with(userRepository.getActiveUser(userId)) {
                    _state.update { it.copy(
                        legitimationText = userName,
                    ) }
                }
            }

            occasionId?.let {
                val image = occasionImageRepository.getImageForOccasion(occasionId)

                with(occasionRepository.getOccasion(occasionId)) {
                    _state.update { it.copy(
                        occasionEntity = this,
                        gotCaught = gotCaught ?: false,
                        numberOfTrapsText = numTraps.toString(),
                        numberOfMiceText = numMice?.toString() ?: "",
                        weatherText = weather ?: "",
                        temperatureText = temperature?.toString() ?: "",
                        legitimationText = leg,
                        noteText = note ?: "",
                        imageId = image?.occasionImgId,
                        imageName = image?.imgName,
                        imageNote = image?.note,
                    ) }
                }
            }

            // for generating next occasion number
            sessionId?.let {
                occasionRepository.getOccasionsForSession(sessionId).collect { occasions ->
                    occasionCount = occasions.size
                }
            }

            fillDropDowns()

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: OccasionScreenEvent) {
        when(event) {
            is OccasionScreenEvent.OnInsertClick -> insertOccasion()

            is OccasionScreenEvent.OnCloudClick -> getWeather()

            OccasionScreenEvent.OnEnvTypeDropDownClick -> {
                _state.update { it.copy(
                    isEnvTypeExpanded = !state.value.isEnvTypeExpanded,
                ) }
            }
            OccasionScreenEvent.OnEnvTypeDropDownDismiss -> {
                _state.update { it.copy(
                    isEnvTypeExpanded = false,
                ) }
            }
            OccasionScreenEvent.OnMethodDropDownClick -> {
                _state.update { it.copy(
                    isMethodExpanded = !state.value.isMethodExpanded,
                ) }
            }
            OccasionScreenEvent.OnMethodDropDownDismiss -> {
                _state.update { it.copy(
                    isMethodExpanded = false,
                ) }
            }
            OccasionScreenEvent.OnMethodTypeDropDownClick -> {
                _state.update { it.copy(
                    isMethodTypeExpanded = !state.value.isMethodTypeExpanded,
                ) }
            }
            OccasionScreenEvent.OnMethodTypeDropDownDismiss -> {
                _state.update { it.copy(
                    isMethodTypeExpanded = false,
                ) }
            }
            OccasionScreenEvent.OnTrapTypeDropDownClick -> {
                _state.update { it.copy(
                    isTrapTypeExpanded = !state.value.isTrapTypeExpanded,
                ) }
            }
            OccasionScreenEvent.OnTrapTypeDropDownDismiss -> {
                _state.update { it.copy(
                    isTrapTypeExpanded = false,
                ) }
            }
            OccasionScreenEvent.OnVegTypeDropDownClick -> {
                _state.update { it.copy(
                    isVegTypeExpanded = !state.value.isVegTypeExpanded,
                ) }
            }
            OccasionScreenEvent.OnVegTypeDropDownDismiss -> {
                _state.update { it.copy(
                    isVegTypeExpanded = false,
                ) }
            }
            is OccasionScreenEvent.OnSelectEnvType -> {
                _state.update { it.copy(
                    envTypeEntity = event.envType,
                ) }
            }
            is OccasionScreenEvent.OnSelectMethod -> {
                _state.update { it.copy(
                    methodEntity = event.method,
                ) }
            }
            is OccasionScreenEvent.OnSelectMethodType -> {
                _state.update { it.copy(
                    methodTypeEntity = event.methodType,
                ) }
            }
            is OccasionScreenEvent.OnSelectTrapType -> {
                _state.update { it.copy(
                    trapTypeEntity = event.trapType,
                ) }
            }
            is OccasionScreenEvent.OnSelectVegType -> {
                _state.update { it.copy(
                    vegTypeEntity = event.vegType,
                ) }
            }
            OccasionScreenEvent.OnGotCaughtClick -> {
                _state.update { it.copy(
                    gotCaught = !state.value.gotCaught,
                ) }
            }
            is OccasionScreenEvent.OnLegitimationChanged -> {
                _state.update { it.copy(
                    legitimationText = event.text,
                    legitimationError = null,
                ) }
            }
            is OccasionScreenEvent.OnNoteChanged -> {
                _state.update { it.copy(
                    noteText = event.text,
                    noteError = null,
                ) }
            }
            is OccasionScreenEvent.OnNumberOfTrapsChanged -> {
                _state.update { it.copy(
                    numberOfTrapsText = event.text,
                    numberOfTrapsError = null,
                ) }
            }
            is OccasionScreenEvent.OnNumberOfMiceChanged -> {
                _state.update { it.copy(
                    numberOfMiceText = event.text,
                    numberOfMiceError = null,
                ) }
            }
            is OccasionScreenEvent.OnWeatherChanged -> {
                _state.update { it.copy(
                    weatherText = event.text,
                    weatherError = null,
                ) }
            }
            is OccasionScreenEvent.OnTemperatureChanged -> {
                _state.update { it.copy(
                    temperatureText = event.text,
                    temperatureError = null,
                ) }
            }
            is OccasionScreenEvent.OnReceiveImageName -> {
                _state.update { it.copy(
                    imageName = event.imageName,
                    imageNote = event.imageNote,
                ) }
            }
            is OccasionScreenEvent.OnLeave -> {
                deleteImageOnLeave()
            }

            else -> Unit
        }
    }

    private fun insertOccasion() {
        with(state.value) {
            val methodID: String = methodEntity?.methodId.ifNullOrBlank {
                _state.update { it.copy(
                    error = "Method has to be selected.",
                ) }
                return
            }!!

            val methodTypeID: String = methodTypeEntity?.methodTypeId.ifNullOrBlank {
                _state.update { it.copy(
                    error = "Method Type has to be selected.",
                ) }
                return
            }!!

            val trapTypeID: String = trapTypeEntity?.trapTypeId.ifNullOrBlank {
                _state.update { it.copy(
                    error = "Trap Type has to be selected.",
                ) }
                return
            }!!

            val leg: String = legitimationText.ifEmpty {
                _state.update { it.copy(
                    legitimationError = "A person needs to sign here.",
                ) }
                return
            }

            val numTraps: Int = Integer.parseInt(
                numberOfTrapsText.ifEmpty {
                    _state.update { it.copy(
                        numberOfTrapsError = "At least one trap needs to exist.",
                    ) }
                    return
                }
            )

            if (sessionId == null) {
                _state.update { it.copy(
                    error = "This should not happen.",
                ) }
                return
            }

            val currentOccasionEntity = occasionEntity

            val occasionEntity = if (currentOccasionEntity == null) {
                OccasionEntity(
                    occasion = (occasionCount + 1),
                    localityID = localityId ?: return,
                    sessionID = sessionId,
                    occasionStart = ZonedDateTime.now(),
                    occasionDateTimeCreated = ZonedDateTime.now(),
                    occasionDateTimeUpdated = null,

                    numMice = numberOfMiceText.toIntOrNull(),
                    methodID = methodID,
                    methodTypeID = methodTypeID,
                    trapTypeID = trapTypeID,
                    envTypeID = envTypeEntity?.envTypeId,
                    vegetTypeID = vegTypeEntity?.vegetTypeId,
                    leg = leg,
                    gotCaught = gotCaught,
                    numTraps = numTraps,
                    temperature = temperatureText.toFloatOrNull(),
                    weather = weatherText.ifEmpty { null },
                    note = noteText.ifEmpty { null },
                )
            } else {
                OccasionEntity(
                    occasionId = currentOccasionEntity.occasionId,
                    occasion = currentOccasionEntity.occasion,
                    localityID = currentOccasionEntity.localityID,
                    sessionID = currentOccasionEntity.sessionID,
                    occasionStart = currentOccasionEntity.occasionStart,
                    occasionDateTimeCreated = currentOccasionEntity.occasionDateTimeCreated,
                    occasionDateTimeUpdated = ZonedDateTime.now(),

                    numMice = numberOfMiceText.toIntOrNull(),
                    methodID = methodID,
                    methodTypeID = methodTypeID,
                    trapTypeID = trapTypeID,
                    envTypeID = envTypeEntity?.envTypeId,
                    vegetTypeID = vegTypeEntity?.vegetTypeId,
                    leg = leg,
                    gotCaught = gotCaught,
                    numTraps = numTraps,
                    temperature = temperatureText.toFloatOrNull(),
                    weather = weatherText.ifEmpty { null },
                    note = noteText.ifEmpty { null },
                )
            }

            viewModelScope.launch(Dispatchers.IO) {
                insertOccasionUseCase(sessionId, occasionEntity, imageName, imageNote)
                _eventFlow.emit(UiEvent.NavigateBack)
            }
        }
    }

    private fun getWeather() {
        localityId?.let {
            viewModelScope.launch {
                with(localityRepository.getLocality(localityId)) {
                    getWeatherUseCase(
                        occasionEntity = state.value.occasionEntity,
                        latitude = latitudeA?.toDouble(),
                        longitude = longitudeA?.toDouble(),
                    ).collect { result ->
                        result
                            .onSuccess { weather ->
                                _state.update { it.copy(
                                    weatherText = weather.weather ?: "",
                                    temperatureText = weather.temp?.toString() ?: "",
                                ) }
                            }
                            .onError { error ->
                                _state.update { it.copy(
                                    error = error.toString(),
                                ) }
                            }
                    }
                }
            }
        }
    }

    private suspend fun fillDropDowns() {
        envTypeRepository.getSettingsEntityList().collect { envList ->
            _state.update { it.copy(
                envTypeList = envList.filterIsInstance<EnvTypeEntity>(),
            ) }
        }

        methodRepository.getSettingsEntityList().collect { methodList ->
            _state.update { it.copy(
                methodList = methodList.filterIsInstance<MethodEntity>(),
            ) }
        }

        methodTypeRepository.getSettingsEntityList().collect { methodTypeList ->
            _state.update { it.copy(
                methodTypeList = methodTypeList.filterIsInstance<MethodTypeEntity>(),
            ) }
        }

        trapTypeRepository.getSettingsEntityList().collect { trapTypeList ->
            _state.update { it.copy(
                trapTypeList = trapTypeList.filterIsInstance<TrapTypeEntity>(),
            ) }
        }

        vegetTypeRepository.getSettingsEntityList().collect { vegTypeList ->
            _state.update { it.copy(
                vegTypeList = vegTypeList.filterIsInstance<VegetTypeEntity>(),
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