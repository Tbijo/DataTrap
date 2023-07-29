package com.example.datatrap.occasion.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OccasionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOccasion(occasion: Occasion): Long

    @Update
    suspend fun updateOccasion(occasion: Occasion)

    @Query("DELETE FROM Occasion WHERE occasionId = :occasionId")
    suspend fun deleteOccasion(occasionId: Long)

    @Query("SELECT * FROM Occasion WHERE occasionId = :occasionId")
    fun getOccasion(occasionId: Long): LiveData<Occasion>

    @Query("SELECT Occasion.occasion AS occasionNum, Occasion.occasionStart AS occasionStart, Occasion.gotCaught AS gotCaught, Occasion.numTraps AS numTraps, Occasion.numMice AS numMice, Occasion.temperature AS temperature, Occasion.weather AS weather, Occasion.leg AS leg, Occasion.note AS note, Locality.localityName AS locality, Method.methodName AS method, MethodType.methodTypeName AS methodType, TrapType.trapTypeName AS trapType, EnvType.envTypeName AS envType, VegetType.vegetTypeName AS vegetType FROM Occasion INNER JOIN Locality ON Locality.localityId = Occasion.localityID INNER JOIN Method ON Method.methodId = Occasion.methodID INNER JOIN MethodType ON MethodType.methodTypeId = Occasion.methodTypeID INNER JOIN TrapType ON TrapType.trapTypeId = Occasion.trapTypeID LEFT JOIN EnvType ON EnvType.envTypeId = Occasion.envTypeID LEFT JOIN VegetType ON VegetType.vegetTypeId = Occasion.vegetTypeID WHERE Occasion.occasionId = :occasionId")
    fun getOccasionView(occasionId: Long): LiveData<OccasionView>

    @Query("SELECT occasionId, localityID, occasion, occasionStart, numMice, numTraps FROM Occasion WHERE sessionID = :idSession")
    fun getOccasionsForSession(idSession: Long): LiveData<List<OccList>>

    // SYNC
    @Query("SELECT * FROM Occasion WHERE occasionId IN (:occasionIds)")
    suspend fun getOccasionForSync(occasionIds: List<Long>): List<Occasion>

    @Query("SELECT * FROM occasion WHERE sessionID = :sessionID AND occasionStart = :occasionStart")
    suspend fun getSyncOccasion(sessionID: Long, occasionStart: Long): Occasion?

}