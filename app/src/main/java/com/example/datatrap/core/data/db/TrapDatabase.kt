package com.example.datatrap.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.datatrap.camera.data.mouse_image.MouseImageDao
import com.example.datatrap.camera.data.mouse_image.MouseImageEntity
import com.example.datatrap.camera.data.occasion_image.OccasionImageDao
import com.example.datatrap.camera.data.occasion_image.OccasionImageEntity
import com.example.datatrap.core.data.locality_session.LocalitySessionCrossRef
import com.example.datatrap.core.data.locality_session.LocalitySessionDao
import com.example.datatrap.core.data.project_locality.ProjectLocalityCrossRef
import com.example.datatrap.core.data.project_locality.ProjectLocalityDao
import com.example.datatrap.locality.data.locality.LocalityDao
import com.example.datatrap.locality.data.locality.LocalityEntity
import com.example.datatrap.mouse.data.MouseDao
import com.example.datatrap.mouse.data.MouseEntity
import com.example.datatrap.occasion.data.occasion.OccasionDao
import com.example.datatrap.occasion.data.occasion.OccasionEntity
import com.example.datatrap.project.data.ProjectDao
import com.example.datatrap.project.data.ProjectEntity
import com.example.datatrap.session.data.SessionDao
import com.example.datatrap.session.data.SessionEntity
import com.example.datatrap.settings.data.env_type.EnvTypeDao
import com.example.datatrap.settings.data.env_type.EnvTypeEntity
import com.example.datatrap.settings.data.method.MethodDao
import com.example.datatrap.settings.data.method.MethodEntity
import com.example.datatrap.settings.data.methodtype.MethodTypeDao
import com.example.datatrap.settings.data.methodtype.MethodTypeEntity
import com.example.datatrap.settings.data.protocol.ProtocolDao
import com.example.datatrap.settings.data.protocol.ProtocolEntity
import com.example.datatrap.settings.data.traptype.TrapTypeDao
import com.example.datatrap.settings.data.traptype.TrapTypeEntity
import com.example.datatrap.settings.data.veg_type.VegetTypeDao
import com.example.datatrap.settings.data.veg_type.VegetTypeEntity
import com.example.datatrap.settings.user.data.UserDao
import com.example.datatrap.settings.user.data.UserEntity
import com.example.datatrap.specie.data.SpecieDao
import com.example.datatrap.specie.data.SpecieEntity
import com.example.datatrap.specie.data.specie_image.SpecieImageDao
import com.example.datatrap.specie.data.specie_image.SpecieImageEntity

@Database(entities = [
    EnvTypeEntity::class, LocalityEntity::class, MethodEntity::class, MethodTypeEntity::class,
    OccasionEntity::class, ProjectEntity::class, ProtocolEntity::class, SessionEntity::class,
    SpecieEntity::class, TrapTypeEntity::class, VegetTypeEntity::class, MouseEntity::class,
    UserEntity::class, MouseImageEntity::class, OccasionImageEntity::class, SpecieImageEntity::class,
    LocalitySessionCrossRef::class, ProjectLocalityCrossRef::class
                     ], version = 1, exportSchema = false
)
@TypeConverters(DateStringConverters::class, UriStringConverters::class)
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
    abstract fun userDao(): UserDao
    abstract fun mouseImageDao(): MouseImageDao
    abstract fun occasionImageDao(): OccasionImageDao
    abstract fun specieImageDao(): SpecieImageDao
    abstract fun localitySessionDao(): LocalitySessionDao
    abstract fun projectLocalityDao(): ProjectLocalityDao

}