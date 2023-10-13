package com.example.datatrap.occasion.presentation.occasion_add_edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.data.pref.PrefRepository
import com.example.datatrap.core.presentation.util.UiEvent
import com.example.datatrap.core.util.Resource
import com.example.datatrap.core.util.ifNullOrBlank
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.data.weather.WeatherRepository
import com.example.datatrap.occasion.domain.use_case.GetWeatherUseCase
import com.example.datatrap.occasion.navigation.OccasionScreens
import com.example.datatrap.settings.envtype.data.EnvTypeRepository
import com.example.datatrap.settings.method.data.MethodRepository
import com.example.datatrap.settings.methodtype.data.MethodTypeRepository
import com.example.datatrap.settings.traptype.data.TrapTypeRepository
import com.example.datatrap.settings.user.data.UserRepository
import com.example.datatrap.settings.vegettype.data.VegetTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class OccasionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val occasionRepository: OccasionRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val weatherRepository: WeatherRepository,
    private val getWeatherUseCase: GetWeatherUseCase = GetWeatherUseCase(weatherRepository),
    private val localityRepository: LocalityRepository,
    private val envTypeRepository: EnvTypeRepository,
    private val methodRepository: MethodRepository,
    private val methodTypeRepository: MethodTypeRepository,
    private val trapTypeRepository: TrapTypeRepository,
    private val vegetTypeRepository: VegetTypeRepository,
    private val userRepository: UserRepository,
    private val prefRepository: PrefRepository,
): ViewModel() {

    // private var occasionImageEntity: OccasionImageEntity? = null

    // number of occasions in a session to calculate occasionNum of a new OccasionEntity
    private var occasionCount: Int = 0
    private var sessionID: String? = null
    private var localityID: String? = null

    private val _state = MutableStateFlow(OccasionUiState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionScreen.occasionIdKey, null).onEach { occId ->
            occId?.let { occasionId ->
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
                    ) }
                }
            }
        }.launchIn(viewModelScope)

        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionScreen.localityIdKey, null).onEach { locId ->
            locId?.let { localityId ->
                localityID = localityId
            }
        }.launchIn(viewModelScope)

        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionScreen.sessionIdKey, null).onEach { sesId ->
            sesId?.let { sessionId ->
                sessionID = sessionId
                occasionRepository.getOccasionsForSession(sessionId).collectLatest {
                    occasionCount = it.size
                }
            }
        }.launchIn(viewModelScope)

        fillDropDowns()

        prefRepository.readUserId().onEach { userId ->
            userId?.let {
                userRepository.getActiveUser(userId).onEach { userEntity ->
                    _state.update { it.copy(
                        legitimationText = userEntity.userName
                    ) }
                }
            }
        }.launchIn(viewModelScope)

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

            else -> Unit
        }
    }

    private fun insertOccasion() {
        val methodID: String = state.value.methodEntity?.methodId.ifNullOrBlank {
            _state.update { it.copy(
                error = "Method has to be selected."
            ) }
            return
        }!!

        val methodTypeID: String = state.value.methodTypeEntity?.methodTypeId.ifNullOrBlank {
            _state.update { it.copy(
                error = "Method Type has to be selected."
            ) }
            return
        }!!

        val trapTypeID: String = state.value.trapTypeEntity?.trapTypeId.ifNullOrBlank {
            _state.update { it.copy(
                error = "Trap Type has to be selected."
            ) }
            return
        }!!

        val leg: String = state.value.legitimationText.ifEmpty {
            _state.update { it.copy(
                legitimationError = "A person needs to sign here."
            ) }
            return
        }

        val numTraps: Int = Integer.parseInt(state.value.numberOfTrapsText.ifEmpty {
            _state.update { it.copy(
                numberOfTrapsError = "At least one trap needs to exist."
            ) }
            return
        })

        val occasionEntity = if (state.value.occasionEntity == null) {
            OccasionEntity(
                occasion = (occasionCount + 1),
                localityID = localityID ?: return,
                sessionID = sessionID ?: return,
                occasionStart = ZonedDateTime.now(),
                occasionDateTimeCreated = ZonedDateTime.now(),
                occasionDateTimeUpdated = null,

                numMice = state.value.numberOfMiceText.toIntOrNull(),
                methodID = methodID,
                methodTypeID = methodTypeID,
                trapTypeID = trapTypeID,
                envTypeID = state.value.envTypeEntity?.envTypeId,
                vegetTypeID = state.value.vegTypeEntity?.vegetTypeId,
                leg = leg,
                gotCaught = state.value.gotCaught,
                numTraps = numTraps,
                temperature = state.value.temperatureText.toFloatOrNull(),
                weather = state.value.weatherText.ifEmpty { null },
                note = state.value.noteText.ifEmpty { null },
            )
        } else {
            OccasionEntity(
                occasionId = state.value.occasionEntity?.occasionId ?: return,
                occasion = state.value.occasionEntity?.occasion ?: return,
                localityID = state.value.occasionEntity?.localityID ?: return,
                sessionID = state.value.occasionEntity?.sessionID ?: return,
                occasionStart = state.value.occasionEntity?.occasionStart ?: return,
                occasionDateTimeCreated = state.value.occasionEntity?.occasionDateTimeCreated ?: return,
                occasionDateTimeUpdated = ZonedDateTime.now(),

                numMice = state.value.numberOfMiceText.toIntOrNull(),
                methodID = methodID,
                methodTypeID = methodTypeID,
                trapTypeID = trapTypeID,
                envTypeID = state.value.envTypeEntity?.envTypeId,
                vegetTypeID = state.value.vegTypeEntity?.vegetTypeId,
                leg = leg,
                gotCaught = state.value.gotCaught,
                numTraps = numTraps,
                temperature = state.value.temperatureText.toFloatOrNull(),
                weather = state.value.weatherText.ifEmpty { null },
                note = state.value.noteText.ifEmpty { null },
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            occasionRepository.insertOccasion(occasionEntity)
            _eventFlow.emit(UiEvent.NavigateBack)
        }
    }

    private fun getWeather() {
        viewModelScope.launch {
            with(localityRepository.getLocality(localityID!!)) {
                getWeatherUseCase(
                    occasionEntity = state.value.occasionEntity,
                    latitude = xA?.toDouble(),
                    longitude = yA?.toDouble(),
                ).collect { result ->
                    when(result) {
                        is Resource.Error -> {
                            _state.update { it.copy(
                                error = result.throwable?.message.toString()
                            ) }
                        }
                        is Resource.Success -> {
                            _state.update { it.copy(
                                weatherText = result.data?.weather ?: "",
                                temperatureText = result.data?.temp?.toString() ?: ""
                            ) }
                        }
                    }
                }
            }
        }
    }

    private fun fillDropDowns() {
        envTypeRepository.getEnvTypeEntityList().onEach { envList ->
            _state.update { it.copy(
                envTypeList = envList,
            ) }
        }.launchIn(viewModelScope)

        methodRepository.methodEntityList().onEach { methodList ->
            _state.update { it.copy(
                methodList = methodList,
            ) }
        }.launchIn(viewModelScope)

        methodTypeRepository.methodTypeEntityList().onEach { methodTypeList ->
            _state.update { it.copy(
                methodTypeList = methodTypeList,
            ) }
        }.launchIn(viewModelScope)

        trapTypeRepository.getTrapTypeEntityList().onEach { trapTypeList ->
            _state.update { it.copy(
                trapTypeList = trapTypeList,
            ) }
        }.launchIn(viewModelScope)

        vegetTypeRepository.getVegetTypeEntityList().onEach { vegTypeList ->
            _state.update { it.copy(
                vegTypeList = vegTypeList,
            ) }
        }.launchIn(viewModelScope)
    }

}