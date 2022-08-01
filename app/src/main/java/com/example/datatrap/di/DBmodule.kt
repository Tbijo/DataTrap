package com.example.datatrap.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.datatrap.databaseio.TrapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // zivotnost v ramci aplikacie
object DBmodule {

    @Singleton
    @Provides
    fun provideTrapDatabase(
        @ApplicationContext context: Context
    ): TrapDatabase {
        return Room.databaseBuilder(
            context,
            TrapDatabase::class.java,
            "trap_database"
        )
            .createFromAsset("database/init_database.db")
            .addCallback(CALLBACK)
            .build()
    }

    private val CALLBACK = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Mouse Insert, po vytvoreni vlastnej mysi mouseIid sa musi rovnat mouseId
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnMouseInsertUpdateMouseIid AFTER INSERT ON Mouse WHEN NEW.mouseIid = 0 BEGIN UPDATE Mouse SET mouseIid = NEW.mouseId WHERE mouseId = NEW.mouseId; END")
            // Mouse Insert, po pridani novej mysi sa updatne numMice+1 v Projecte
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnMouseInsertUpdateProject BEFORE INSERT ON Mouse WHEN NEW.primeMouseID IS NULL BEGIN UPDATE Project SET numMice = (numMice + 1), projectDateTimeUpdated = (strftime('%s','now') * 1000) WHERE projectId = (SELECT projectID FROM Session WHERE sessionId = (SELECT sessionID FROM Occasion WHERE occasionId = NEW.occasionID)); END")
            // Mouse Insert, po pridani novej mysi sa updatne numMice+1 v Occasion
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnMouseInsertUpdateOccasion BEFORE INSERT ON Mouse WHEN NEW.primeMouseID IS NULL BEGIN UPDATE Occasion SET numMice = (numMice + 1), occasionDateTimeUpdated = (strftime('%s','now') * 1000) WHERE occasionId = NEW.occasionID; END")
            // Mouse Insert, po pridani novej mysi ak neni specieID = TRE, Close, P, PVP, Other tak updatne sa gotCaught v Occasion
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnMouseInsertUpdateOccasionGotCaught BEFORE INSERT ON Mouse WHEN (NEW.speciesID NOT IN (SELECT specieId FROM Specie WHERE speciesCode = 'TRE' OR speciesCode = 'C' OR speciesCode = 'PVP' OR speciesCode = 'P' OR speciesCode = 'Other')) AND ((SELECT gotCaught FROM Occasion WHERE occasionId = NEW.occasionID) = 0) BEGIN UPDATE Occasion SET gotCaught = 1, occasionDateTimeUpdated = (strftime('%s','now') * 1000) WHERE occasionId = NEW.occasionID; END")

            // Mouse Recap Insert, po pridani ak je sex prveho zaznamu iny ako aktualny tak sa vsetky nastavia podla aktualneho
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnMouseInsertChangeSexIfNecesary BEFORE INSERT ON Mouse WHEN NEW.primeMouseID IS NOT NULL AND ((NEW.sex IS NOT NULL AND (SELECT sex FROM Mouse WHERE mouseId = NEW.primeMouseID) IS NOT NULL AND NEW.sex <> (SELECT sex FROM Mouse WHERE mouseId = NEW.primeMouseID)) OR (NEW.sex IS NULL AND (SELECT sex FROM Mouse WHERE mouseId = NEW.primeMouseID) IS NOT NULL) OR (NEW.sex IS NOT NULL AND (SELECT sex FROM Mouse WHERE mouseId = NEW.primeMouseID) IS NULL)) BEGIN UPDATE Mouse SET sex = NEW.sex, mouseDateTimeUpdated = (strftime('%s','now') * 1000) WHERE primeMouseID = NEW.primeMouseID OR mouseId = NEW.primeMouseID; END")

            // Delete Mouse, po vymazani update Project numMice - 1
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnMouseDeleteUpdateProject BEFORE DELETE ON Mouse WHEN OLD.primeMouseID IS NULL BEGIN UPDATE Project SET numMice = (numMice - 1), projectDateTimeUpdated = (strftime('%s','now') * 1000) WHERE projectId = (SELECT projectID FROM Session WHERE sessionId = (SELECT sessionID FROM Occasion WHERE occasionId = OLD.occasionID)); END")
            // Delete Mouse, po vymazani update Occasion numMice - 1
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnMouseDeleteUpdateOccasion BEFORE DELETE ON Mouse WHEN OLD.primeMouseID IS NULL BEGIN UPDATE Occasion SET numMice = (numMice - 1), occasionDateTimeUpdated = (strftime('%s','now') * 1000) WHERE occasionId = OLD.occasionID; END")

            // Delete Occasion, po vymazani update Session numOcc - 1
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnOccasionDeleteUpdateSession BEFORE DELETE ON Occasion BEGIN UPDATE Session SET numOcc = (numOcc - 1), sessionDateTimeUpdated = (strftime('%s','now') * 1000) WHERE sessionId = OLD.sessionID; END")
            // Delete Occasion, po vymazani update Project zniz o pocet mysi v Occasion
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnOccasionDeleteUpdateProject BEFORE DELETE ON Occasion BEGIN UPDATE Project SET numMice = (numMice - OLD.numMice), projectDateTimeUpdated = (strftime('%s','now') * 1000) WHERE projectId = (SELECT projectID FROM Session WHERE sessionId = OLD.sessionID); END")
            // Insert Occasion, po pridani update Session numOcc + 1
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnOccasionInsertUpdateSession BEFORE INSERT ON Occasion BEGIN UPDATE Session SET numOcc = (numOcc + 1), sessionDateTimeUpdated = (strftime('%s','now') * 1000) WHERE sessionId = NEW.sessionID; END")

            // Delete PrjLocCrossRef, po vymazani kombinacie sa updatne Project numLocal - 1
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnProjectLocalityCrossRefDeleteUpdateProject BEFORE DELETE ON ProjectLocalityCrossRef BEGIN UPDATE Project SET numLocal = (numLocal - 1), projectDateTimeUpdated = (strftime('%s','now') * 1000) WHERE projectId = OLD.projectId; END")
            // Insert PrjLocCrossRef, po pridani kombinacie sa updatne Project numLocal + 1
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnProjectLocalityCrossRefInsertUpdateProject BEFORE INSERT ON ProjectLocalityCrossRef BEGIN UPDATE Project SET numLocal = (numLocal + 1), projectDateTimeUpdated = (strftime('%s','now') * 1000) WHERE projectId = NEW.projectId; END")

            // DELETE Session, po vymazani update Project znizit numMice o vsetky pocty mysi v jej Occasionov
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnSessionDeleteUpdateProject BEFORE DELETE ON Session BEGIN UPDATE Project SET numMice = (numMice - (SELECT CASE WHEN (SUM(numMice)) IS NULL THEN 0 ELSE SUM(numMice) END result FROM Occasion WHERE sessionID = OLD.sessionId)), projectDateTimeUpdated = (strftime('%s','now') * 1000) WHERE projectId = OLD.projectID; END")
            // DELETE LocSessCrossRef, po vymazani update Locality numSession - 1
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnLocSessDeleteUpdateLocality BEFORE DELETE ON LocalitySessionCrossRef BEGIN UPDATE Locality SET numSessions = numSessions - 1, localityDateTimeUpdated = (strftime('%s','now') * 1000) WHERE localityId = OLD.localityId; END")
            // INSERT LocSessCrossRef, po pridani update Locality numSession + 1
            db.execSQL("CREATE TRIGGER IF NOT EXISTS OnLocSessInsertUpdateLocality BEFORE INSERT ON LocalitySessionCrossRef BEGIN UPDATE Locality SET numSessions = numSessions + 1, localityDateTimeUpdated = (strftime('%s','now') * 1000) WHERE localityId = NEW.localityId; END")
        }
    }

}