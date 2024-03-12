package com.example.datatrap.occasion.presentation.occasion_add_edit

import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.settings.data.env_type.EnvTypeEntity
import com.example.datatrap.settings.data.method.MethodEntity
import com.example.datatrap.settings.data.methodtype.MethodTypeEntity
import com.example.datatrap.settings.data.traptype.TrapTypeEntity
import com.example.datatrap.settings.data.veg_type.VegetTypeEntity

data class OccasionUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val occasionEntity: OccasionEntity? = null,

    val envTypeList: List<EnvTypeEntity> = emptyList(),
    val envTypeEntity: EnvTypeEntity? = null,
    val isEnvTypeExpanded: Boolean = false,

    val methodList: List<MethodEntity> = emptyList(),
    val methodEntity: MethodEntity? = null,
    val isMethodExpanded: Boolean = false,

    val methodTypeList: List<MethodTypeEntity> = emptyList(),
    val methodTypeEntity: MethodTypeEntity? = null,
    val isMethodTypeExpanded: Boolean = false,

    val trapTypeList: List<TrapTypeEntity> = emptyList(),
    val trapTypeEntity: TrapTypeEntity? = null,
    val isTrapTypeExpanded: Boolean = false,

    val vegTypeList: List<VegetTypeEntity> = emptyList(),
    val vegTypeEntity: VegetTypeEntity? = null,
    val isVegTypeExpanded: Boolean = false,

    val gotCaught: Boolean = false,

    val numberOfTrapsText: String = "",
    val numberOfTrapsError: String? = null,

    val numberOfMiceText: String = "",
    val numberOfMiceError: String? = null,

    val weatherText: String = "",
    val weatherError: String? = null,

    val temperatureText: String = "",
    val temperatureError: String? = null,

    val legitimationText: String = "",
    val legitimationError: String? = null,

    val noteText: String = "",
    val noteError: String? = null,

    val imageName: String? = null,
    val imageNote: String? = null,
)
