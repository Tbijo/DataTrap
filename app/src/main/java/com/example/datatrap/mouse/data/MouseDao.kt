package com.example.datatrap.mouse.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.core.util.Constants.MILLISECONDS_IN_2_YEAR
import com.example.datatrap.mouse.domain.model.MouseLog
import com.example.datatrap.mouse.domain.model.MouseOccList
import com.example.datatrap.mouse.domain.model.MouseRecapList
import com.example.datatrap.mouse.domain.model.MouseView

@Dao
interface MouseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMouse(mouse: Mouse): Long

    @Update
    suspend fun updateMouse(mouse: Mouse)

    @Query("DELETE FROM Mouse WHERE mouseId = :mouseId")
    suspend fun deleteMouse(mouseId: Long)

    @Query("SELECT * FROM Mouse WHERE mouseId = :mouseId")
    fun getMouse(mouseId: Long): LiveData<Mouse>

    // zobrazit vybrane stlpce v mouseView
    @Query("SELECT Mouse.mouseIid AS mouseIid, Mouse.deviceID AS deviceID, Mouse.body AS body, Mouse.tail AS tail, Mouse.feet AS feet, Mouse.ear AS ear, Mouse.mouseCaught AS mouseCaught, Mouse.gravidity AS gravidity, Mouse.lactating AS lactating, Mouse.sexActive AS sexActive, Mouse.age AS age, Mouse.sex AS sex, Mouse.weight AS weight, Mouse.note AS note, Mouse.testesLength AS testesLength, Mouse.testesWidth AS testesWidth, Mouse.embryoRight AS embryoRight, Mouse.embryoLeft AS embryoLeft, Mouse.embryoDiameter AS embryoDiameter, Mouse.MC AS mc, Mouse.MCright as mcRight, Mouse.MCleft AS mcLeft, Specie.fullName AS specieFullName, Specie.speciesCode AS specieCode, Occasion.leg AS legit, Project.projectName AS projectName FROM Mouse INNER JOIN Specie ON Specie.specieId = Mouse.speciesID INNER JOIN Occasion ON Occasion.occasionId = Mouse.occasionID INNER JOIN Session ON Session.sessionId = Occasion.sessionID INNER JOIN Project ON Project.projectId = Session.projectID WHERE Mouse.mouseId = :idMouse")
    fun getMouseForView(idMouse: Long): LiveData<MouseView>

    // zobrazit predch zaznamy mysi v mouseView
    @Query("SELECT Mouse.mouseCaught AS mouseCaught, Locality.localityName AS localityName, Mouse.trapID AS trapID, Mouse.weight AS weight, Mouse.sexActive AS sexActive, Mouse.gravidity AS gravidity, Mouse.lactating AS lactating FROM Mouse INNER JOIN Locality ON Locality.localityId = Mouse.localityID WHERE ((Mouse.primeMouseID IS NOT NULL AND Mouse.primeMouseID = :primeMouseID) OR (Mouse.mouseIid = :primeMouseID)) AND Mouse.deviceID = :deviceID ORDER BY Mouse.mouseCaught ASC")
    fun getMiceForLog(primeMouseID: Long, deviceID: String): LiveData<List<MouseLog>>

    // zobrazit vsetky mysi viazane na Occasion
    @Query("SELECT Mouse.mouseId AS mouseId, Mouse.primeMouseID AS primeMouseID, Mouse.code AS mouseCode, Specie.speciesCode AS specieCode, Mouse.mouseCaught AS mouseCaught, Mouse.sex AS sex, Mouse.deviceID AS deviceID FROM Mouse INNER JOIN Specie ON Specie.specieId = Mouse.speciesID WHERE Mouse.occasionID = :idOccasion")
    fun getMiceForOccasion(idOccasion: Long): LiveData<List<MouseOccList>>

    // zobrazit chytene mys podla ich Kodu na znovu chytenie a nesmu byt starsie ako dva roky //currentTime: Long
    @Query("SELECT Mouse.mouseId AS mouseId, Mouse.primeMouseID AS primeMouseID, Mouse.code AS code, Mouse.age AS age, Mouse.weight AS weight, Mouse.sex AS sex, Mouse.gravidity AS gravidity, Mouse.lactating AS lactating, Mouse.sexActive AS sexActive, Locality.localityName AS localityName, Specie.speciesCode AS specieCode, Mouse.mouseCaught AS mouseCaught FROM Mouse INNER JOIN Locality ON Locality.localityId = Mouse.localityID INNER JOIN Specie ON Specie.specieId = Mouse.speciesID WHERE (Mouse.code = :code OR :code IS NULL) AND (Mouse.speciesID = :specieID OR :specieID IS NULL) AND (Mouse.sex = :sex OR :sex IS NULL) AND (Mouse.age = :age OR :age IS NULL) AND Mouse.gravidity = :gravidity AND Mouse.lactating = :lactating AND Mouse.sexActive = :sexActive AND ((Mouse.mouseCaught BETWEEN :dateFrom AND :dateTo) OR (:dateFrom IS NULL AND :dateTo IS NULL)) AND (:currentTime - mouseCaught) < $MILLISECONDS_IN_2_YEAR ORDER BY Mouse.mouseCaught DESC")
    fun getMiceForRecapture(code: Int?, specieID: Long?, sex: String?, age: String?, gravidity: Boolean, sexActive: Boolean,lactating: Boolean, dateFrom: Long?, dateTo: Long?, currentTime: Long): LiveData<List<MouseRecapList>>

    // zoznam kodov ktore nie su starsie ako dva roky = reprezentuju zivych potkanov
    @Query("SELECT code FROM Mouse WHERE localityID = :localityId AND Code IS NOT NULL AND (:currentTime - mouseCaught) < $MILLISECONDS_IN_2_YEAR")
    fun getActiveCodeOfLocality(localityId: Long, currentTime: Long): LiveData<List<Int>>

    // pocet mysi v lokalite
    @Query("SELECT COUNT(Code) FROM Mouse WHERE localityID = :localityId AND Code IS NOT NULL AND Recapture = 0")
    fun countMiceForLocality(localityId: Long): LiveData<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultiMouse(mice: List<Mouse>)

    // cisla pasci pouzitych na occasion
    @Query("SELECT trapID FROM Mouse WHERE occasionID = :occasionId")
    fun getTrapsIdInOccasion(occasionId: Long): LiveData<List<Int>>

    // SYNC
    // zoznam novych alebo upravenych mysi podla posledneho datumu sync
    @Query("SELECT * FROM Mouse WHERE mouseDateTimeCreated >= :lastSync OR mouseDateTimeUpdated >= :lastSync")
    suspend fun getMiceForSync(lastSync: Long): List<Mouse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserSynctMouse(mouse: Mouse)

    @Query("SELECT * FROM mouse WHERE occasionID = :occasionID AND mouseCaught = :mouseCaught")
    suspend fun getSyncMouse(occasionID: Long, mouseCaught: Long): Mouse?

}