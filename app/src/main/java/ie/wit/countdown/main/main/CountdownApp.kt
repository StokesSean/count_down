package ie.wit.countdown.main.main

import android.app.Application
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import org.jetbrains.anko.AnkoLogger

class CountdownApp : Application(), AnkoLogger {
    lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    override fun onCreate() {
        super.onCreate()
        Log.v("Countdown","Countdown has started")
    }
}