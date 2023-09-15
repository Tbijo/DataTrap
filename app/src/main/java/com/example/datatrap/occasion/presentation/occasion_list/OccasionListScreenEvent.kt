package com.example.datatrap.occasion.presentation.occasion_list

import com.example.datatrap.occasion.data.occasion.OccasionEntity

sealed interface OccasionListScreenEvent {
    data class OnItemClick(val occasionEntity: OccasionEntity): OccasionListScreenEvent
    data class OnUpdateButtonClick(val occasionEntity: OccasionEntity): OccasionListScreenEvent
    data class OnDeleteClick(val occasionEntity: OccasionEntity): OccasionListScreenEvent
    object OnAddButtonClick: OccasionListScreenEvent
    data class OnDetailButtonClick(val occasionEntity: OccasionEntity): OccasionListScreenEvent
}