package com.example.datatrap.occasion.presentation.occasion_detail

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.util.EnumSpecie
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.navigation.OccasionScreens
import com.example.datatrap.settings.envtype.data.EnvTypeRepository
import com.example.datatrap.settings.method.data.MethodRepository
import com.example.datatrap.settings.methodtype.data.MethodTypeRepository
import com.example.datatrap.settings.traptype.data.TrapTypeRepository
import com.example.datatrap.settings.vegettype.data.VegetTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OccasionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val occasionRepository: OccasionRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val mouseRepository: MouseRepository,
    private val localityRepository: LocalityRepository,
    private val methodRepository: MethodRepository,
    private val methodTypeRepository: MethodTypeRepository,
    private val envTypeRepository: EnvTypeRepository,
    private val vegetTypeRepository: VegetTypeRepository,
    private val trapTypeRepository: TrapTypeRepository,
): ViewModel() {

    private var path: String? = null

    private val _state = MutableStateFlow(OccasionDetailUiState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionDetailScreen.occasionIdKey, null).onEach { occasionId ->
            occasionId?.let {
                mouseRepository.getMiceForOccasion(occasionId).collect { miceList ->
                    val specieCodeStr = EnumSpecie.values().map { it.name }

                    _state.update { it.copy(
                        errorNum = miceList.count { mouse -> mouse.specieCode == EnumSpecie.TRE.name },
                        closeNum = miceList.count { mouse -> mouse.specieCode == EnumSpecie.C.name },
                        predatorNum = miceList.count { mouse -> mouse.specieCode == EnumSpecie.P.name },
                        pvpNum = miceList.count { mouse -> mouse.specieCode == EnumSpecie.PVP.name },
                        otherNum = miceList.count { mouse -> mouse.specieCode == EnumSpecie.Other.name },
                        specieNum = miceList.count { mouse -> mouse.specieCode !in specieCodeStr },
                    ) }
                }

                occasionRepository.getOccasion(occasionId).collect { occasion ->
                    _state.update { it.copy(
                        occasionEntity = occasion,
                    ) }

                    methodRepository.getMethod(occasion.methodID).collect { method ->
                        _state.update { it.copy(
                            methodName = method.methodName,
                        ) }
                    }
                    methodTypeRepository.getMethodType(occasion.methodTypeID).collect { methodType ->
                        _state.update { it.copy(
                            methodName = methodType.methodTypeName,
                        ) }
                    }
                    occasion.envTypeID?.let { envTypeID ->
                        envTypeRepository.getEnvType(envTypeID).collect { envType ->
                            _state.update { it.copy(
                                envTypeName = envType.envTypeName,
                            ) }
                        }
                    }
                    occasion.vegetTypeID?.let { vegetTypeID ->
                        vegetTypeRepository.getVegetType(vegetTypeID).collect { vegType ->
                            _state.update { it.copy(
                                vegTypeName = vegType.vegetTypeName,
                            ) }
                        }
                    }
                    trapTypeRepository.getTrapType(occasion.trapTypeID).collect { trapType ->
                        _state.update { it.copy(
                            trapTypeName = trapType.trapTypeName,
                        ) }
                    }
                }

                occasionImageRepository.getImageForOccasion(occasionId).collect { occImage ->
                    occImage?.let {
                        _state.update { it.copy(
                            imagePath = occImage.path.toUri().path
                        ) }
                        path = occImage.path
                    }
                }
            }
        }.launchIn(viewModelScope)

        savedStateHandle.getStateFlow<String?>(OccasionScreens.OccasionDetailScreen.localityIdKey, null).onEach { locId ->
            locId?.let {
                localityRepository.getLocality(locId).collect { locality ->
                    _state.update { it.copy(
                        localityName = locality.localityName,
                    ) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: OccasionDetailScreenEvent) {
        when(event) {
            OccasionDetailScreenEvent.OnImageClick -> {
                _state.update { it.copy(
                    isSheetExpanded = !state.value.isSheetExpanded
                ) }
            }
        }
    }

}