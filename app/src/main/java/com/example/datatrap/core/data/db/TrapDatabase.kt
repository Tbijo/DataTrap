package com.example.datatrap.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.datatrap.databaseio.dao.*
import com.example.datatrap.locality.data.Locality
import com.example.datatrap.locality.data.LocalityDao
import com.example.datatrap.project.data.ProjectLocalityCrossRef
import com.example.datatrap.project.data.ProjectLocalityDao
import com.example.datatrap.models.*
import com.example.datatrap.mouse.data.Mouse
import com.example.datatrap.mouse.data.MouseDao
import com.example.datatrap.occasion.data.Occasion
import com.example.datatrap.occasion.data.OccasionDao
import com.example.datatrap.camera.data.occasion_image.OccasionImage
import com.example.datatrap.camera.data.occasion_image.OccasionImageDao
import com.example.datatrap.camera.data.mouse_image.MouseImage
import com.example.datatrap.specie.data.specie_image.SpecieImage
import com.example.datatrap.camera.data.mouse_image.MouseImageDao
import com.example.datatrap.specie.data.specie_image.SpecieImageDao
import com.example.datatrap.project.data.Project
import com.example.datatrap.project.data.ProjectDao
import com.example.datatrap.session.data.LocalitySessionDao
import com.example.datatrap.session.data.Session
import com.example.datatrap.session.data.SessionDao
import com.example.datatrap.settings.envtype.data.EnvType
import com.example.datatrap.settings.envtype.data.EnvTypeDao
import com.example.datatrap.settings.method.data.Method
import com.example.datatrap.settings.method.data.MethodDao
import com.example.datatrap.settings.methodtype.data.MethodType
import com.example.datatrap.settings.methodtype.data.MethodTypeDao
import com.example.datatrap.settings.protocol.data.Protocol
import com.example.datatrap.settings.protocol.data.ProtocolDao
import com.example.datatrap.settings.traptype.data.TrapType
import com.example.datatrap.settings.traptype.data.TrapTypeDao
import com.example.datatrap.settings.user.data.User
import com.example.datatrap.settings.user.data.UserDao
import com.example.datatrap.settings.vegettype.data.VegetType
import com.example.datatrap.settings.vegettype.data.VegetTypeDao
import com.example.datatrap.specie.data.Specie
import com.example.datatrap.specie.data.SpecieDao

@Database(entities = [
    EnvType::class, Locality::class, Method::class, MethodType::class,
    Occasion::class, Project::class, Protocol::class, Session::class,
    Specie::class, TrapType::class, VegetType::class, Mouse::class,
    ProjectLocalityCrossRef::class, User::class, LocalitySessionCrossRef::class,
    MouseImage::class, OccasionImage::class, SpecieImage::class
                     ], version = 1, exportSchema = false
)
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

}