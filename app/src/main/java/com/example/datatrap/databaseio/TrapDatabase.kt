package com.example.datatrap.databaseio

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.datatrap.databaseio.converters.DateLongConverters
import com.example.datatrap.databaseio.dao.*
import com.example.datatrap.models.projectlocality.ProjectLocalityCrossRef
import com.example.datatrap.databaseio.dao.ProjectLocalityDao
import com.example.datatrap.models.*
import com.example.datatrap.models.localitysession.LocalitySessionCrossRef

@Database(entities = [
    EnvType::class, Locality::class, Method::class, MethodType::class,
    Occasion::class, Project::class, Protocol::class, Session::class,
    Specie::class, TrapType::class, VegetType::class, Mouse::class,
    ProjectLocalityCrossRef::class, User::class, LocalitySessionCrossRef::class,
    MouseImage::class, OccasionImage::class, SpecieImage::class, SynchronizeDate::class
                     ], version = 1, exportSchema = false)
@TypeConverters(DateLongConverters::class)
abstract class TrapDatabase: RoomDatabase() {

    abstract fun envTypeDao(): EnvTypeDao
    abstract fun localityDao(): LocalityDao
    abstract fun methodDao(): MethodDao
    abstract fun methodTypeDao(): MethodTypeDao
    abstract fun mouseDao(): MouseDao
    abstract fun occasionDao(): OccasionDao
    abstract fun projectDao(): ProjectDao
    abstract fun protocolDao(): ProtocolDao
    abstract fun sessionDao(): SessionDao
    abstract fun specieDao(): SpecieDao
    abstract fun trapTypeDao(): TrapTypeDao
    abstract fun vegetTypeDao(): VegetTypeDao
    abstract fun projectLocalityDao(): ProjectLocalityDao
    abstract fun userDao(): UserDao
    abstract fun localitySessionDao(): LocalitySessionDao
    abstract fun mouseImageDao(): MouseImageDao
    abstract fun occasionImageDao(): OccasionImageDao
    abstract fun specieImageDao(): SpecieImageDao
    abstract fun synchronizeDao(): SynchronizeDateDao

}