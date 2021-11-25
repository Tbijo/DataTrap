package com.example.datatrap.databaseio.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.datatrap.models.Mouse
import com.example.datatrap.models.tuples.MouseLog
import com.example.datatrap.models.tuples.MouseOccList
import com.example.datatrap.models.tuples.MouseRecapList
import com.example.datatrap.models.tuples.MouseView

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
    @Query("SELECT Mouse.body AS body, Mouse.tail AS tail, Mouse.feet AS feet, Mouse.ear AS ear, Mouse.mouseDateTimeCreated AS mouseDateTime, Mouse.gravidity AS gravidity, Mouse.lactating AS lactating, Mouse.sexActive AS sexActive, Mouse.age AS age, Mouse.sex AS sex, Mouse.weight AS weight, Mouse.note AS note, Mouse.testesLength AS testesLength, Mouse.testesWidth AS testesWidth, Mouse.embryoRight AS embryoRight, Mouse.embryoLeft AS embryoLeft, Mouse.embryoDiameter AS embryoDiameter, Mouse.MC AS mc, Mouse.MCright as mcRight, Mouse.MCleft AS mcLeft, Specie.fullName AS specieFullName, Specie.speciesCode AS specieCode, Occasion.leg AS legit, Project.projectName AS projectName FROM Mouse INNER JOIN Specie ON Specie.specieId = Mouse.speciesID INNER JOIN Occasion ON Occasion.occasionId = Mouse.occasionID INNER JOIN Session ON Session.sessionId = Occasion.sessionID INNER JOIN Project ON Project.projectId = Session.projectID WHERE Mouse.mouseId = :idMouse AND Mouse.deviceID = :deviceID")
    fun getMouseForView(idMouse: Long, deviceID: String): LiveData<MouseView>

    // zobrazit predch zaznamy mysi v mouseView
    @Query("SELECT Mouse.mouseDateTimeCreated AS mouseDateTimeCreated, Locality.localityName AS localityName, Mouse.trapID AS trapID, Mouse.weight AS weight, Mouse.sexActive AS sexActive, Mouse.gravidity AS gravidity, Mouse.lactating AS lactating FROM Mouse INNER JOIN Locality ON Locality.localityId = Mouse.localityID WHERE ((Mouse.primeMouseID IS NOT NULL AND Mouse.primeMouseID = :primeMouseID) OR (Mouse.mouseId = :primeMouseID)) AND Mouse.deviceID = :deviceID ORDER BY Mouse.mouseDateTimeCreated ASC")
    fun getMiceForLog(primeMouseID: Long, deviceID: String): LiveData<List<MouseLog>>

    // zobrazit vsetky mysi viazane na Occasion
    @Query("SELECT Mouse.mouseId AS mouseId, Mouse.primeMouseID AS primeMouseID, Mouse.code AS mouseCode, Specie.speciesCode AS specieCode, Mouse.mouseDateTimeCreated AS dateTime, Mouse.sex AS sex FROM Mouse INNER JOIN Specie ON Specie.specieId = Mouse.speciesID WHERE Mouse.occasionID = :idOccasion")
    fun getMiceForOccasion(idOccasion: Long): LiveData<List<MouseOccList>>

    // zobrazit chytene mys podla ich Kodu na znovu chytenie a nesmu byt starsie ako dva roky
    @Query("SELECT Mouse.mouseId AS mouseId, Mouse.primeMouseID AS primeMouseID, Mouse.code AS code, Mouse.age AS age, Mouse.weight AS weight, Mouse.sex AS sex, Mouse.gravidity AS gravidity, Mouse.lactating AS lactating, Mouse.sexActive AS sexActive, Locality.localityName AS localityName, Specie.speciesCode AS specieCode, Mouse.mouseDateTimeCreated AS dateTime FROM Mouse INNER JOIN Locality ON Locality.localityId = Mouse.localityID INNER JOIN Specie ON Specie.specieId = Mouse.speciesID WHERE Mouse.Code = :code AND (:currentTime - mouseDateTimeCreated) < :twoYears ORDER BY Mouse.mouseDateTimeCreated DESC LIMIT 300")
    fun getMiceForRecapture(code: Int, currentTime: Long, twoYears: Long): LiveData<List<MouseRecapList>>

    // zoznam kodov ktore nie su starsie ako dva roky = reprezentuju zivych potkanov
    @Query("SELECT code FROM Mouse WHERE localityID = :localityId AND Code IS NOT NULL AND (:currentTime - mouseDateTimeCreated) < :twoYears")
    fun getActiveCodeOfLocality(localityId: Long, currentTime: Long, twoYears: Long): LiveData<List<Int>>

    // pocet mysi v lokalite
    @Query("SELECT COUNT(Code) FROM Mouse WHERE localityID = :localityId AND Code IS NOT NULL AND Recapture = 0")
    fun countMiceForLocality(localityId: Long): LiveData<Int>
}