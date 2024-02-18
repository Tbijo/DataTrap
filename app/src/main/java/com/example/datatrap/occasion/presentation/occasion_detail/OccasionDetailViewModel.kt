package com.example.datatrap.occasion.presentation.occasion_detail

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.getMainScreenNavArgs
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.domain.use_case.CountSpecialCasesUseCase
import com.example.datatrap.settings.data.env_type.EnvTypeRepository
import com.example.datatrap.settings.data.method.MethodRepository
import com.example.datatrap.settings.data.methodtype.MethodTypeRepository
import com.example.datatrap.settings.data.traptype.TrapTypeRepository
import com.example.datatrap.settings.data.veg_type.VegetTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OccasionDetailViewModel @Inject constructor(
    private val occasionRepository: OccasionRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val mouseRepository: MouseRepository,
    private val localityRepository: LocalityRepository,
    private val methodRepository: MethodRepository,
    private val methodTypeRepository: MethodTypeRepository,
    private val envTypeRepository: EnvTypeRepository,
    private val vegetTypeRepository: VegetTypeRepository,
    private val trapTypeRepository: TrapTypeRepository,
    private val countSpecialCasesUseCase: CountSpecialCasesUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {

    private var imagePath: String? = null

    private val _state = MutableStateFlow(OccasionDetailUiState())
    val state = _state.asStateFlow()

    private val occasionId = savedStateHandle.getMainScreenNavArgs()?.occasionId
    private val localityId = savedStateHandle.getMainScreenNavArgs()?.localityId

    init {
        viewModelScope.launch(Dispatchers.IO) {
            occasionId?.let {
                countSpecialCasesUseCase(occasionId).collect {
                    _state.update { it.copy(
                        errorNum = it.errorNum,
                        closeNum = it.closeNum,
                        predatorNum = it.predatorNum,
                        pvpNum = it.pvpNum,
                        otherNum = it.otherNum,
                        specieNum = it.specieNum,
                    ) }
                }

                with(occasionRepository.getOccasion(occasionId)) {
                    _state.update { it.copy(
                        occasionEntity = this,
                    ) }

                    methodRepository.getMethod(methodID).collect { method ->
                        _state.update { it.copy(
                            methodName = method.methodName,
                        ) }
                    }
                    methodTypeRepository.getMethodType(methodTypeID).collect { methodType ->
                        _state.update { it.copy(
                            methodName = methodType.methodTypeName,
                        ) }
                    }
                    envTypeID?.let { envTypeID ->
                        envTypeRepository.getEnvType(envTypeID).collect { envType ->
                            _state.update { it.copy(
                                envTypeName = envType.envTypeName,
                            ) }
                        }
                    }
                    vegetTypeID?.let { vegetTypeID ->
                        vegetTypeRepository.getVegetType(vegetTypeID).collect { vegType ->
                            _state.update { it.copy(
                                vegTypeName = vegType.vegetTypeName,
                            ) }
                        }
                    }
                    trapTypeRepository.getTrapType(trapTypeID).collect { trapType ->
                        _state.update { it.copy(
                            trapTypeName = trapType.trapTypeName,
                        ) }
                    }
                }

                with(occasionImageRepository.getImageForOccasion(occasionId)) {
                    this?.let {
                        _state.update { it.copy(
                            imagePath = path.toUri().path
                        ) }
                        imagePath = path
                    }
                }
            }

            localityId?.let {
                with(localityRepository.getLocality(localityId)) {
                    _state.update { it.copy(
                        localityName = localityName,
                    ) }
                }
            }

            _state.update { it.copy(
                isLoading = false,
            ) }
        }
    }

    fun onEvent(event: OccasionDetailScreenEvent) {
        when(event) {
            OccasionDetailScreenEvent.OnImageClick -> {
                _state.update { it.copy(
                    isSheetExpanded = !state.value.isSheetExpanded,
                ) }
            }
        }
    }

}