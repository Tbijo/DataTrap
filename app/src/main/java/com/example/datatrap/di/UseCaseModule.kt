package com.example.datatrap.di

import com.example.datatrap.camera.data.mouse_image.MouseImageRepository
import com.example.datatrap.core.data.pref.PrefRepository
import com.example.datatrap.core.domain.GetInfoNamesUseCase
import com.example.datatrap.locality.data.locality.LocalityRepository
import com.example.datatrap.mouse.data.MouseRepository
import com.example.datatrap.mouse.domain.use_case.DeleteMouseUseCase
import com.example.datatrap.mouse.domain.use_case.GenerateCodeUseCase
import com.example.datatrap.mouse.domain.use_case.GetMiceByOccasion
import com.example.datatrap.mouse.domain.use_case.GetMiceForRecapture
import com.example.datatrap.mouse.domain.use_case.GetMouseDetail
import com.example.datatrap.mouse.domain.use_case.GetOccupiedTrapIdsInOccasion
import com.example.datatrap.mouse.domain.use_case.GetPreviousLogsOfMouse
import com.example.datatrap.occasion.data.occasion.OccasionRepository
import com.example.datatrap.occasion.data.weather.WeatherRepository
import com.example.datatrap.occasion.domain.use_case.GetWeatherUseCase
import com.example.datatrap.project.data.ProjectRepository
import com.example.datatrap.session.data.SessionRepository
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
    ): DeleteMouseUseCase {
        return DeleteMouseUseCase(
            mouseRepository,
            mouseImageRepository,
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
        prefRepository: PrefRepository,
    ): GenerateCodeUseCase {
        return GenerateCodeUseCase(
            mouseRepository,
            prefRepository,
        )
    }
}