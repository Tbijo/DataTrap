package com.example.datatrap.www

import com.example.datatrap.models.sync.MouseImageSync
import com.example.datatrap.models.sync.OccasionImageSync
import com.example.datatrap.models.sync.SpecieImageSync
import com.example.datatrap.models.sync.SyncClass

data class SyncData(
    var listSyncClass: List<SyncClass>,
    var listMouseImageSync: List<MouseImageSync>,
    var listOccasionImageSync: List<OccasionImageSync>,
    var listSpecieImageSync: List<SpecieImageSync>,
)
