package com.edge.integrated

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.edge.integrated.service.TermuxService
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class EdgeIntegratedApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDirectories()
    }

    private fun initializeDirectories() {
        val dirs = listOf(
            "models",
            "termux/home",
            "termux/usr",
            "winlator/containers",
            "winlator/wine",
            "opencode/config",
            "opencode/sessions",
            "downloads"
        )
        dirs.forEach { dir ->
            filesDir.resolve(dir).mkdirs()
        }
    }

    companion object {
        lateinit var instance: EdgeIntegratedApp
            private set

        fun context(): Context = instance.applicationContext
    }
}
