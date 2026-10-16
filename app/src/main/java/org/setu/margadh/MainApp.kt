package org.setu.margadh

import org.setu.margadh.models.MarketJSONStore
import android.app.Application
import timber.log.Timber

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AppData.markets = MarketJSONStore(applicationContext)
        Timber.i("Margadh started")
    }
}