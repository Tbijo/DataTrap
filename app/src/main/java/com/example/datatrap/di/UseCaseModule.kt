package com.example.datatrap.di

import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.camera.data.occasion_image.OccasionImageRepository
import com.example.datatrap.camera.domain.DeleteImageUseCase
import com.example.datatrap.core.data.locality_session.LocalitySessionRepository
import com.example.datatrap.core.data.project_locality.ProjectLocalityRepository
import com.example.datatrap.core.data.shared_nav_args.NavArgsStorage
import com.example.datatrap.core.data.storage.InternalStorageRepository
import com.example.datatrap.core.domain.use_case.DeleteLocalitySessionUseCase
import com.example.datatrap.core.domain.use_case.DeleteProjectLocalityUseCase
import com.example.datatrap.core.domain.use_case.GetInfoNamesUseCase
import com.example.datatrap.core.domain.use_case.InsertLocalitySessionUseCase
import com.example.datatrap.core.domain.use_case.InsertProjectLocalityUseCase
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.use_case.DeleteMouseUseCase
import com.example.datatrap.mouse.domain.use_case.GenerateCodeUseCase
import com.example.datatrap.mouse.domain.use_case.GetMiceByOccasion
import com.example.datatrap.mouse.domain.use_case.GetMiceForRecapture
import com.example.datatrap.mouse.domain.use_case.GetMouseDetail
import com.example.datatrap.mouse.domain.use_case.GetOccupiedTrapIdsInOccasion
import com.example.datatrap.mouse.domain.use_case.GetPreviousLogsOfMouse
import com.example.datatrap.mouse.domain.use_case.InsertMouseUseCase
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.data.weather.WeatherRepository
import com.example.datatrap.occasion.domain.use_case.CountSpecialSpeciesUseCase
import com.example.datatrap.occasion.domain.use_case.DeleteOccasionUseCase
import com.example.datatrap.occasion.domain.use_case.GetWeatherUseCase
import com.example.datatrap.occasion.domain.use_case.InsertOccasionUseCase
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
import com.example.datatrap.session.domain.DeleteSessionUseCase
import com.example.datatrap.specie.data.SpecieRepository
import com.example.datatrap.specie.data.specie_image.SpecieImageRepository
import com.example.datatrap.specie.domain.use_case.DeleteSpecieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {

    @ActivityRetainedScoped
    @Provides
    fun provideGetMouseDetail(
        mouseRepository: MouseRepository,
        specieRepository: SpecieRepository,
        occasionRepository: OccasionRepository,
        sessionRepository: SessionRepository,
        projectRepository: ProjectRepository,
    ): GetMouseDetail {
        return GetMouseDetail(
            mouseRepository,
            specieRepository,
            occasionRepository,
            sessionRepository,
            projectRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetWeatherUseCase(
        weatherRepository: WeatherRepository,
    ): GetWeatherUseCase {
        return GetWeatherUseCase(
            weatherRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetMiceForRecapture(
        mouseRepository: MouseRepository,
        localityRepository: LocalityRepository,
        specieRepository: SpecieRepository,
    ): GetMiceForRecapture {
        return GetMiceForRecapture(
            mouseRepository,
            localityRepository,
            specieRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteSpecieUseCase(
        specieRepository: SpecieRepository,
        specieImageRepository: SpecieImageRepository,
    ): DeleteSpecieUseCase {
        return DeleteSpecieUseCase(
            specieRepository,
            specieImageRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetPreviousLogsOfMouse(
        mouseRepository: MouseRepository,
        localityRepository: LocalityRepository,
    ): GetPreviousLogsOfMouse {
        return GetPreviousLogsOfMouse(
            mouseRepository,
            localityRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetMiceByOccasion(
        mouseRepository: MouseRepository,
        specieRepository: SpecieRepository,
    ): GetMiceByOccasion {
        return GetMiceByOccasion(
            mouseRepository,
            specieRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteMouseUseCase(
        mouseRepository: MouseRepository,
        mouseImageRepository: MouseImageRepository,
        projectRepository: ProjectRepository,
        sessionRepository: SessionRepository,
        occasionRepository: OccasionRepository,
    ): DeleteMouseUseCase {
        return DeleteMouseUseCase(
            mouseRepository,
            mouseImageRepository,
            projectRepository,
            sessionRepository,
            occasionRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetInfoNamesUseCase(
        projectRepository: ProjectRepository,
        localityRepository: LocalityRepository,
        sessionRepository: SessionRepository,
        occasionRepository: OccasionRepository,
    ): GetInfoNamesUseCase {
        return GetInfoNamesUseCase(
            projectRepository,
            localityRepository,
            sessionRepository,
            occasionRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGetOccupiedTrapIdsInOccasion(
        mouseRepository: MouseRepository,
    ): GetOccupiedTrapIdsInOccasion {
        return GetOccupiedTrapIdsInOccasion(
            mouseRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideGenerateCodeUseCase(
        mouseRepository: MouseRepository,
        navArgsStorage: NavArgsStorage,
    ): GenerateCodeUseCase {
        return GenerateCodeUseCase(
            mouseRepository,
            navArgsStorage,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideInsertMouseUseCase(
        mouseRepository: MouseRepository,
        projectRepository: ProjectRepository,
        sessionRepository: SessionRepository,
        occasionRepository: OccasionRepository,
        specieRepository: SpecieRepository,
        mouseImageRepository: MouseImageRepository,
        internalStorageRepository: InternalStorageRepository
    ): InsertMouseUseCase {
        return InsertMouseUseCase(
            mouseRepository,
            projectRepository,
            sessionRepository,
            occasionRepository,
            specieRepository,
            mouseImageRepository,
            internalStorageRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteOccasionUseCase(
        occasionRepository: OccasionRepository,
        occasionImageRepository: OccasionImageRepository,
        sessionRepository: SessionRepository,
        projectRepository: ProjectRepository,
    ): DeleteOccasionUseCase {
        return DeleteOccasionUseCase(
            occasionRepository,
            occasionImageRepository,
            sessionRepository,
            projectRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideInsertOccasionUseCase(
        sessionRepository: SessionRepository,
        occasionRepository: OccasionRepository,
        occasionImageRepository: OccasionImageRepository,
        internalStorageRepository: InternalStorageRepository,
    ): InsertOccasionUseCase {
        return InsertOccasionUseCase(
            occasionRepository,
            sessionRepository,
            occasionImageRepository,
            internalStorageRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteSessionUseCase(
        projectRepository: ProjectRepository,
        occasionRepository: OccasionRepository,
    ): DeleteSessionUseCase {
        return DeleteSessionUseCase(
            projectRepository,
            occasionRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteProjectLocalityUseCase(
        projectLocalityRepository: ProjectLocalityRepository,
        projectRepository: ProjectRepository,
    ): DeleteProjectLocalityUseCase {
        return DeleteProjectLocalityUseCase(
            projectLocalityRepository,
            projectRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideInsertProjectLocalityUseCase(
        projectLocalityRepository: ProjectLocalityRepository,
        projectRepository: ProjectRepository,
    ): InsertProjectLocalityUseCase {
        return InsertProjectLocalityUseCase(
            projectLocalityRepository,
            projectRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideDeleteLocalitySessionUseCase(
        localitySessionRepository: LocalitySessionRepository,
        localityRepository: LocalityRepository,
    ): DeleteLocalitySessionUseCase {
        return DeleteLocalitySessionUseCase(
            localitySessionRepository,
            localityRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideInsertLocalitySessionUseCase(
        localitySessionRepository: LocalitySessionRepository,
        localityRepository: LocalityRepository,
    ): InsertLocalitySessionUseCase {
        return InsertLocalitySessionUseCase(
            localitySessionRepository,
            localityRepository,
        )
    }
    @ActivityRetainedScoped
    @Provides
    fun provideDeleteImageUseCase(
        internalStorageRepository: InternalStorageRepository,
        occasionImageRepository: OccasionImageRepository,
        mouseImageRepository: MouseImageRepository,
    ): DeleteImageUseCase {
        return DeleteImageUseCase(
            internalStorageRepository,
            occasionImageRepository,
            mouseImageRepository,
        )
    }

    @ActivityRetainedScoped
    @Provides
    fun provideCountSpecialCasesUseCase(
        mouseRepository: MouseRepository,
        specieRepository: SpecieRepository,
    ): CountSpecialSpeciesUseCase {
        return CountSpecialSpeciesUseCase(
            mouseRepository = mouseRepository,
            specieRepository = specieRepository,
        )
    }
}