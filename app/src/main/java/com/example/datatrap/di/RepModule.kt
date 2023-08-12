package com.example.datatrap.di

import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.core.data.db.TrapDatabase
import com.example.datatrap.locality.data.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.occasion.data.OccasionRepository
import com.example.datatrap.project.data.ProjectLocalityRepository
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.settings.envtype.data.EnvTypeRepository
import com.example.datatrap.settings.method.data.MethodRepository
import com.example.datatrap.settings.methodtype.data.MethodTypeRepository
import com.example.datatrap.settings.protocol.data.ProtocolRepository
import com.example.datatrap.settings.traptype.data.TrapTypeRepository
import com.example.datatrap.settings.user.data.UserRepository
import com.example.datatrap.settings.vegettype.data.VegetTypeRepository
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
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
    fun provideMouseImageRep(db: TrapDatabase) = MouseImageRepository(db.mouseImageDao())

    @ActivityRetainedScoped
    @Provides
    fun provideOccasionImageRep(db: TrapDatabase) = OccasionImageRepository(db.occasionImageDao())

    @ActivityRetainedScoped
    @Provides
    fun provideSpecieImageRep(db: TrapDatabase) = SpecieImageRepository(db.specieImageDao())

}