package com.example.datatrap.mouse.data

import androidx.room.*
import com.example.datatrap.core.util.Constants.MILLISECONDS_IN_2_YEAR
import com.example.datatrap.mouse.domain.model.MouseLog
import com.example.datatrap.mouse.domain.model.MouseOccList
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.mouse.domain.model.MouseView
import kotlinx.coroutines.flow.Flow

@Dao
interface MouseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMouse(mouseEntity: MouseEntity): String

    @Delete
    suspend fun deleteMouse(mouseEntity: MouseEntity)

    @Query("SELECT * FROM MouseEntity WHERE mouseId = :mouseId")
    fun getMouse(mouseId: Long): Flow<MouseEntity>

    // zobrazit vybrane stlpce v mouseView
    @Query(
        "SELECT MouseEntity.mouseIid AS mouseIid, MouseEntity.deviceID AS deviceID, MouseEntity.body AS body, MouseEntity.tail AS tail, MouseEntity.feet AS feet, MouseEntity.ear AS ear, MouseEntity.mouseCaught AS mouseCaught, MouseEntity.gravidity AS gravidity, MouseEntity.lactating AS lactating, MouseEntity.sexActive AS sexActive, MouseEntity.age AS age, MouseEntity.sex AS sex, MouseEntity.weight AS weight, MouseEntity.note AS note, MouseEntity.testesLength AS testesLength, MouseEntity.testesWidth AS testesWidth, MouseEntity.embryoRight AS embryoRight, MouseEntity.embryoLeft AS embryoLeft, MouseEntity.embryoDiameter AS embryoDiameter, MouseEntity.MC AS mc, MouseEntity.MCright as mcRight, MouseEntity.MCleft AS mcLeft, SpecieEntity.fullName AS specieFullName, SpecieEntity.speciesCode AS specieCode, OccasionEntity.leg AS legit, ProjectEntity.projectName AS projectName FROM MouseEntity INNER JOIN SpecieEntity ON SpecieEntity.specieId = MouseEntity.speciesID INNER JOIN OccasionEntity ON OccasionEntity.occasionId = MouseEntity.occasionID INNER JOIN SessionEntity ON SessionEntity.sessionId = OccasionEntity.sessionID INNER JOIN ProjectEntity ON ProjectEntity.projectId = SessionEntity.projectID WHERE MouseEntity.mouseId = :idMouse"
    )
    fun getMouseForView(idMouse: Long): Flow<MouseView>

    // zobrazit predch zaznamy mysi v mouseView
    @Query("SELECT MouseEntity.mouseCaught AS mouseCaught, LocalityEntity.localityName AS localityName, MouseEntity.trapID AS trapID, MouseEntity.weight AS weight, MouseEntity.sexActive AS sexActive, MouseEntity.gravidity AS gravidity, MouseEntity.lactating AS lactating FROM MouseEntity INNER JOIN LocalityEntity ON LocalityEntity.localityId = MouseEntity.localityID WHERE ((MouseEntity.primeMouseID IS NOT NULL AND MouseEntity.primeMouseID = :primeMouseID) OR (MouseEntity.mouseIid = :primeMouseID)) AND MouseEntity.deviceID = :deviceID ORDER BY MouseEntity.mouseCaught ASC")
    fun getMiceForLog(primeMouseID: Long, deviceID: String): Flow<List<MouseLog>>

    // zobrazit vsetky mysi viazane na Occasion
    @Query("SELECT MouseEntity.mouseId AS mouseId, MouseEntity.primeMouseID AS primeMouseID, MouseEntity.code AS mouseCode, SpecieEntity.speciesCode AS specieCode, MouseEntity.mouseCaught AS mouseCaught, MouseEntity.sex AS sex, MouseEntity.deviceID AS deviceID FROM MouseEntity INNER JOIN SpecieEntity ON SpecieEntity.specieId = MouseEntity.speciesID WHERE MouseEntity.occasionID = :idOccasion")
    fun getMiceForOccasion(idOccasion: Long): Flow<List<MouseOccList>>

    // zobrazit chytene mys podla ich Kodu na znovu chytenie a nesmu byt starsie ako dva roky //currentTime: Long
    @Query(
        "SELECT MouseEntity.mouseId AS mouseId, MouseEntity.primeMouseID AS primeMouseID, MouseEntity.code AS code, MouseEntity.age AS age, MouseEntity.weight AS weight, MouseEntity.sex AS sex, MouseEntity.gravidity AS gravidity, MouseEntity.lactating AS lactating, MouseEntity.sexActive AS sexActive, LocalityEntity.localityName AS localityName, SpecieEntity.speciesCode AS specieCode, MouseEntity.mouseCaught AS mouseCaught FROM MouseEntity INNER JOIN LocalityEntity ON LocalityEntity.localityId = MouseEntity.localityID INNER JOIN SpecieEntity ON SpecieEntity.specieId = MouseEntity.speciesID WHERE (MouseEntity.code = :code OR :code IS NULL) AND (MouseEntity.speciesID = :specieID OR :specieID IS NULL) AND (MouseEntity.sex = :sex OR :sex IS NULL) AND (MouseEntity.age = :age OR :age IS NULL) AND MouseEntity.gravidity = :gravidity AND MouseEntity.lactating = :lactating AND MouseEntity.sexActive = :sexActive AND ((MouseEntity.mouseCaught BETWEEN :dateFrom AND :dateTo) OR (:dateFrom IS NULL AND :dateTo IS NULL)) AND (:currentTime - mouseCaught) < $MILLISECONDS_IN_2_YEAR ORDER BY MouseEntity.mouseCaught DESC"
    )
    fun getMiceForRecapture(code: Int?, specieID: Long?, sex: String?, age: String?, gravidity: Boolean, sexActive: Boolean,lactating: Boolean, dateFrom: Long?, dateTo: Long?, currentTime: Long): Flow<List<MouseRecapList>>

    // zoznam kodov ktore nie su starsie ako dva roky = reprezentuju zivych potkanov
    @Query("SELECT code FROM MouseEntity WHERE localityID = :localityId AND Code IS NOT NULL AND (:currentTime - mouseCaught) < $MILLISECONDS_IN_2_YEAR")
    fun getActiveCodeOfLocality(localityId: Long, currentTime: Long): Flow<List<Int>>

    // pocet mysi v lokalite
    @Query("SELECT COUNT(Code) FROM MouseEntity WHERE localityID = :localityId AND Code IS NOT NULL AND Recapture = 0")
    fun countMiceForLocality(localityId: Long): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiMouse(mice: List<MouseEntity>)

    // cisla pasci pouzitych na occasion
    @Query("SELECT trapID FROM MouseEntity WHERE occasionID = :occasionId")
    fun getTrapsIdInOccasion(occasionId: Long): Flow<List<Int>>

    // SYNC
    // zoznam novych alebo upravenych mysi podla posledneho datumu sync
    @Query("SELECT * FROM MouseEntity WHERE mouseDateTimeCreated >= :lastSync OR mouseDateTimeUpdated >= :lastSync")
    suspend fun getMiceForSync(lastSync: Long): List<MouseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserSynctMouse(mouseEntity: MouseEntity)

    @Query("SELECT * FROM MouseEntity WHERE occasionID = :occasionID AND mouseCaught = :mouseCaught")
    suspend fun getSyncMouse(occasionID: Long, mouseCaught: Long): MouseEntity?

}