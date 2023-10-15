package com.example.datatrap.core.data.project_locality

import androidx.room.*

@Dao
interface ProjectLocalityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectLocalityCrossRef(projectLocalityCrossRef: ProjectLocalityCrossRef)

    @Delete
    suspend fun deleteProjectLocalityCrossRef(projectLocalityCrossRef: ProjectLocalityCrossRef)

    @Query("SELECT EXISTS (SELECT * FROM ProjectLocalityCrossRef WHERE localityId = :localityId AND projectId = :projectId)")
    suspend fun existsProjectLocalityCrossRef(localityId: String, projectId: String): Boolean
}