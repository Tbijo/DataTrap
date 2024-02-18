package com.example.datatrap.di

import android.content.Context
import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.core.data.locality_session.LocalitySessionRepository
import com.example.datatrap.core.data.project_locality.ProjectLocalityRepository
import com.example.datatrap.core.data.storage.ExternalStorageRepository
import com.example.datatrap.core.data.storage.InternalStorageRepository
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.settings.data.env_type.EnvTypeRepository
import com.example.datatrap.settings.data.method.MethodRepository
import com.example.datatrap.settings.data.methodtype.MethodTypeRepository
import com.example.datatrap.settings.data.protocol.ProtocolRepository
import com.example.datatrap.settings.data.traptype.TrapTypeRepository
import com.example.datatrap.settings.data.veg_type.VegetTypeRepository
import com.example.datatrap.settings.user.data.UserRepository
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepModule {

    @ActivityRetainedScoped
    @Provides
    fun provideLocalSessionRep(db: TrapDatabase) = LocalitySessionRepository(db.localitySessionDao())

    @ActivityRetainedScoped
    @Provides
    fun provideProjectLocalRep(db: TrapDatabase) = ProjectLocalityRepository(db.projectLocalityDao())

    @ActivityRetainedScoped
    @Provides
    fun provideEnvTypeRep(db: TrapDatabase) = EnvTypeRepository(db.envTypeDao())

    @ActivityRetainedScoped
    @Provides
    fun provideLocalityRep(db: TrapDatabase) = LocalityRepository(db.localityDao())

    @ActivityRetainedScoped
    @Provides
    fun provideMethodRep(db: TrapDatabase) = MethodRepository(db.methodDao())

    @ActivityRetainedScoped
    @Provides
    fun provideMethodTypeRep(db: TrapDatabase) = MethodTypeRepository(db.methodTypeDao())

    @ActivityRetainedScoped
    @Provides
    fun provideMouseRep(db: TrapDatabase) = MouseRepository(db.mouseDao())

    @ActivityRetainedScoped
    @Provides
    fun provideOccasionRep(db: TrapDatabase) = OccasionRepository(db.occasionDao())

    @ActivityRetainedScoped
    @Provides
    fun provideProjectRep(db: TrapDatabase) = ProjectRepository(db.projectDao())

    @ActivityRetainedScoped
    @Provides
    fun provideProtocolRep(db: TrapDatabase) = ProtocolRepository(db.protocolDao())

    @ActivityRetainedScoped
    @Provides
    fun provideSessionRep(db: TrapDatabase) = SessionRepository(db.sessionDao())

    @ActivityRetainedScoped
    @Provides
    fun provideSpecieRep(db: TrapDatabase) = SpecieRepository(db.specieDao())

    @ActivityRetainedScoped
    @Provides
    fun provideTrapTypeRep(db: TrapDatabase) = TrapTypeRepository(db.trapTypeDao())

    @ActivityRetainedScoped
    @Provides
    fun provideVegetTypeRep(db: TrapDatabase) = VegetTypeRepository(db.vegetTypeDao())

    @ActivityRetainedScoped
    @Provides
    fun provideUserRep(db: TrapDatabase) = UserRepository(db.userDao())

    @ActivityRetainedScoped
    @Provides
    fun provideMouseImageRep(db: TrapDatabase) = MouseImageRepository(db.mouseImageDao())

    @ActivityRetainedScoped
    @Provides
    fun provideOccasionImageRep(db: TrapDatabase) = OccasionImageRepository(db.occasionImageDao())

    @ActivityRetainedScoped
    @Provides
    fun provideSpecieImageRep(db: TrapDatabase) = SpecieImageRepository(db.specieImageDao())

    @ActivityRetainedScoped
    @Provides
    fun provideInternalRep(@ApplicationContext context: Context) = InternalStorageRepository(context)

    @ActivityRetainedScoped
    @Provides
    fun provideExternalRep(@ApplicationContext context: Context) = ExternalStorageRepository(context)

}