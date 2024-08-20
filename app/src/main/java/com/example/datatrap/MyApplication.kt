package com.example.datatrap

import android.app.Application
import com.example.datatrap.camera.di.cameraModule
import com.example.datatrap.di.dataTrapApiModule
import com.example.datatrap.di.otherModule
import com.example.datatrap.di.roomDatabaseModule
import com.example.datatrap.locality.di.localityModule
import com.example.datatrap.login.di.loginModule
import com.example.datatrap.mouse.di.mouseModule
import com.example.datatrap.occasion.di.occasionModule
import com.example.datatrap.project.di.projectModule
import com.example.datatrap.session.di.sessionModule
import com.example.datatrap.settings.di.settingsModule
import com.example.datatrap.specie.di.specieModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                dataTrapApiModule,
                roomDatabaseModule,
                otherModule,
                settingsModule,
                specieModule,
                loginModule,
                projectModule,
                localityModule,
                sessionModule,
                occasionModule,
                mouseModule,
                cameraModule,
            )
        }
    }
}