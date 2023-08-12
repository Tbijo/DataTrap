package com.example.datatrap.occasion.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.occasion.domain.model.OccList
import com.example.datatrap.occasion.domain.model.OccasionView

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccasion(occasionEntity: OccasionEntity): Long

    @Update
    suspend fun updateOccasion(occasionEntity: OccasionEntity)

    @Query("DELETE FROM OccasionEntity WHERE occasionId = :occasionId")
    suspend fun deleteOccasion(occasionId: Long)

    @Query("SELECT * FROM OccasionEntity WHERE occasionId = :occasionId")
    fun getOccasion(occasionId: Long): LiveData<OccasionEntity>

    @Query(
        "SELECT OccasionEntity.occasion AS occasionNum, OccasionEntity.occasionStart AS occasionStart, OccasionEntity.gotCaught AS gotCaught, OccasionEntity.numTraps AS numTraps, OccasionEntity.numMice AS numMice, OccasionEntity.temperature AS temperature, OccasionEntity.weather AS weather, OccasionEntity.leg AS leg, OccasionEntity.note AS note, LocalityEntity.localityName AS locality, MethodEntity.methodName AS method, MethodTypeEntity.methodTypeName AS methodType, TrapTypeEntity.trapTypeName AS trapType, EnvTypeEntity.envTypeName AS envType, VegetTypeEntity.vegetTypeName AS vegetType FROM OccasionEntity INNER JOIN LocalityEntity ON LocalityEntity.localityId = OccasionEntity.localityID INNER JOIN MethodEntity ON MethodEntity.methodId = OccasionEntity.methodID INNER JOIN MethodTypeEntity ON MethodTypeEntity.methodTypeId = OccasionEntity.methodTypeID INNER JOIN TrapTypeEntity ON TrapTypeEntity.trapTypeId = OccasionEntity.trapTypeID LEFT JOIN EnvTypeEntity ON EnvTypeEntity.envTypeId = OccasionEntity.envTypeID LEFT JOIN VegetTypeEntity ON VegetTypeEntity.vegetTypeId = OccasionEntity.vegetTypeID WHERE OccasionEntity.occasionId = :occasionId"
    )
    fun getOccasionView(occasionId: Long): LiveData<OccasionView>

    @Query("SELECT occasionId, localityID, occasion, occasionStart, numMice, numTraps FROM OccasionEntity WHERE sessionID = :idSession")
    fun getOccasionsForSession(idSession: Long): LiveData<List<OccList>>

    // SYNC
    @Query("SELECT * FROM OccasionEntity WHERE occasionId IN (:occasionIds)")
    suspend fun getOccasionForSync(occasionIds: List<Long>): List<OccasionEntity>

    @Query("SELECT * FROM occasion WHERE sessionID = :sessionID AND occasionStart = :occasionStart")
    suspend fun getSyncOccasion(sessionID: Long, occasionStart: Long): OccasionEntity?

}