package com.example.datatrap.di

import com.example.datatrap.databaseio.TrapDatabase
import com.example.datatrap.repositories.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class) // zivotnost len v ramci fragmentov kde ich treba
object RepModule {

    @ActivityRetainedScoped
    @Provides
    fun provideEnvTypeRep(db: TrapDatabase) = EnvTypeRepository(db.envTypeDao())
    // najprv sa vytvori Dao z DBmodule tym sa mysli TrapDatabase a potom z toho sa vytvori tato Repository

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
    fun provideProjectLocalityRep(db: TrapDatabase) = ProjectLocalityRepository(db.projectLocalityDao())

    @ActivityRetainedScoped
    @Provides
    fun provideUserRep(db: TrapDatabase) = UserRepository(db.userDao())

    @ActivityRetainedScoped
    @Provides
    fun provideLocalitySessionRep(db: TrapDatabase) = LocalitySessionRepository(db.localitySessionDao())

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
    fun provideSynchronizeRep(db: TrapDatabase) = SynchronizeDateRepository(db.synchronizeDao())

}