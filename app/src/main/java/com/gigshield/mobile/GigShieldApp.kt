package com.gigshield.mobile

import android.app.Application
import androidx.room.Room
import com.gigshield.mobile.core.database.GigShieldDatabase
import com.gigshield.mobile.core.network.NetworkModule
import com.gigshield.mobile.core.preferences.SessionManager
import com.gigshield.mobile.core.sensor.TelemetrySensorManager
import com.gigshield.mobile.data.repository.AuthRepository
import com.gigshield.mobile.data.repository.ClaimsRepository
import com.gigshield.mobile.data.repository.InsuranceRepository

class GigShieldApp : Application() {

    lateinit var sessionManager: SessionManager
        private set

    lateinit var networkModule: NetworkModule
        private set

    lateinit var database: GigShieldDatabase
        private set

    lateinit var sensorManager: TelemetrySensorManager
        private set

    lateinit var authRepository: AuthRepository
        private set

    lateinit var insuranceRepository: InsuranceRepository
        private set

    lateinit var claimsRepository: ClaimsRepository
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this

        sessionManager = SessionManager(this)
        networkModule = NetworkModule(sessionManager)

        database = Room.databaseBuilder(
            applicationContext,
            GigShieldDatabase::class.java,
            "gigshield_local.db"
        ).fallbackToDestructiveMigration().build()

        sensorManager = TelemetrySensorManager(this)
        sensorManager.startListening()

        authRepository = AuthRepository(
            networkModule = networkModule,
            sessionManager = sessionManager,
            userDao = database.userDao()
        )

        insuranceRepository = InsuranceRepository(
            networkModule = networkModule,
            sessionManager = sessionManager,
            userDao = database.userDao()
        )

        claimsRepository = ClaimsRepository(
            networkModule = networkModule,
            sessionManager = sessionManager,
            claimDao = database.claimDao(),
            userDao = database.userDao()
        )
    }

    companion object {
        lateinit var instance: GigShieldApp
            private set
    }
}
