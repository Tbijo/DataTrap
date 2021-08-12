package com.example.datatrap.databaseio

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.datatrap.databaseio.dao.*
import com.example.datatrap.models.relations.ProjectLocalityCrossRef
import com.example.datatrap.databaseio.dao.ProjectLocalityDao
import com.example.datatrap.models.*

@Database(entities = [
    EnvType::class, Locality::class, Method::class, MethodType::class,
    Occasion::class, Picture::class, Project::class, Protocol::class,
    Session::class, Specie::class, TrapType::class, VegetType::class,
    Mouse::class, ProjectLocalityCrossRef::class
                     ], version = 1, exportSchema = false)
abstract class TrapDatabase: RoomDatabase() {
    abstract fun envTypeDao(): EnvTypeDao
    abstract fun localityDao(): LocalityDao
    abstract fun methodDao(): MethodDao
    abstract fun methodTypeDao(): MethodTypeDao
    abstract fun mouseDao(): MouseDao
    abstract fun occasionDao(): OccasionDao
    abstract fun pictureDao(): PictureDao
    abstract fun projectDao(): ProjectDao
    abstract fun protocolDao(): ProtocolDao
    abstract fun sessionDao(): SessionDao
    abstract fun specieDao(): SpecieDao
    abstract fun trapTypeDao(): TrapTypeDao
    abstract fun vegetTypeDao(): VegetTypeDao
    abstract fun projectLocalityDao(): ProjectLocalityDao

    companion object{
        @Volatile
        private var INSTANCE: TrapDatabase? = null

        fun getDatabase(context: Context): TrapDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TrapDatabase::class.java,
                    "trap_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}