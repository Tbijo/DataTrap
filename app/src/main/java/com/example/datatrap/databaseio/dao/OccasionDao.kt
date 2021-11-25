package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Occasion
import com.example.datatrap.models.tuples.OccList
import com.example.datatrap.models.tuples.OccasionView

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

    @Query("SELECT Occasion.occasion AS occasionNum, Occasion.occasionDateTimeCreated AS dateTime, Occasion.gotCaught AS gotCaught, Occasion.numTraps AS numTraps, Occasion.numMice AS numMice, Occasion.temperature AS temperature, Occasion.weather AS weather, Occasion.leg AS leg, Occasion.note AS note, Locality.localityName AS locality, Method.methodName AS method, MethodType.methodTypeName AS methodType, TrapType.trapTypeName AS trapType, EnvType.envTypeName AS envType, VegetType.vegetTypeName AS vegetType FROM Occasion INNER JOIN Locality ON Locality.localityId = Occasion.localityID INNER JOIN Method ON Method.methodId = Occasion.methodID INNER JOIN MethodType ON MethodType.methodTypeId = Occasion.methodTypeID INNER JOIN TrapType ON TrapType.trapTypeId = Occasion.trapTypeID INNER JOIN EnvType ON EnvType.envTypeId = Occasion.envTypeID INNER JOIN VegetType ON VegetType.vegetTypeId = Occasion.vegetTypeID WHERE occasionId = :occasionId")
    fun getOccasionView(occasionId: Long): LiveData<OccasionView>

    @Query("SELECT occasionId, localityID, occasion, occasionDateTimeCreated AS dateTime, numMice, numTraps FROM Occasion WHERE sessionID = :idSession")
    fun getOccasionsForSession(idSession: Long): LiveData<List<OccList>>

}