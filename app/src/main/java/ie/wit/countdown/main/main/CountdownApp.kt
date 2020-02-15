package ie.wit.countdown.main.main

import android.app.Application
import android.util.Log
import ie.wit.countdown.main.models.CountdownMemStore
import ie.wit.countdown.main.models.CountdownStore

class CountdownApp : Application() {
    lateinit var countdownstore: CountdownStore
    override fun onCreate() {
        super.onCreate()
        countdownstore = CountdownMemStore()
        Log.v("Countdown","Countdown has started")
    }
}