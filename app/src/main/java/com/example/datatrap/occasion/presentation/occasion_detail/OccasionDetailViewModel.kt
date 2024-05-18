package com.example.datatrap.occasion.presentation.occasion_detail

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.domain.use_case.CountSpecialSpeciesUseCase
import com.example.datatrap.settings.data.env_type.EnvTypeRepository
import com.example.datatrap.settings.data.method.MethodRepository
import com.example.datatrap.settings.data.methodtype.MethodTypeRepository
import com.example.datatrap.settings.data.traptype.TrapTypeRepository
import com.example.datatrap.settings.data.veg_type.VegetTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OccasionDetailViewModel(
    private val occasionRepository: OccasionRepository,
    private val occasionImageRepository: OccasionImageRepository,
    private val localityRepository: LocalityRepository,
    private val methodRepository: MethodRepository,
    private val methodTypeRepository: MethodTypeRepository,
    private val envTypeRepository: EnvTypeRepository,
    private val vegetTypeRepository: VegetTypeRepository,
    private val trapTypeRepository: TrapTypeRepository,
    private val countSpecialSpeciesUseCase: CountSpecialSpeciesUseCase,
    private val occasionId: String?,
    private val localityId: String?,
): ViewModel() {

    private var imagePath: String? = null

    private val _state = MutableStateFlow(OccasionDetailUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            occasionId?.let {
                countSpecialSpeciesUseCase(occasionId).collect {
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

                    _state.update { it.copy(
                        methodName = methodRepository.getSettingsEntity(methodID).entityName,
                    ) }

                    _state.update { it.copy(
                        methodTypeName = methodTypeRepository.getSettingsEntity(methodTypeID).entityName,
                    ) }

                    envTypeID?.let { envTypeID ->
                        _state.update { it.copy(
                            envTypeName = envTypeRepository.getSettingsEntity(envTypeID).entityName,
                        ) }
                    }

                    vegetTypeID?.let { vegetTypeID ->
                        _state.update { it.copy(
                            vegTypeName = vegetTypeRepository.getSettingsEntity(vegetTypeID).entityName,
                        ) }
                    }

                    _state.update { it.copy(
                        trapTypeName = trapTypeRepository.getSettingsEntity(trapTypeID).entityName,
                    ) }
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