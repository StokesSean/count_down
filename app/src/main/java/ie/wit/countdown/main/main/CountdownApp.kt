package ie.wit.countdown.main.main

import android.app.Application
import android.util.Log
import ie.wit.countdown.main.models.CountdownStore
import ie.wit.countdown.main.models.CountdownMemStore

class CountdownApp : Application() {

    lateinit var countdownstore: CountdownStore

    override fun onCreate() {
        super.onCreate()
        countdownstore = CountdownMemStore()
        Log.v("Countdown","Countdown has started")
    }


}