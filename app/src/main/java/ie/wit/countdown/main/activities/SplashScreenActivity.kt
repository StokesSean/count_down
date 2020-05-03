package ie.wit.countdown.main.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import ie.wit.countdown.R
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        handler = Handler()
        handler.postDelayed({

            val intent = Intent(this, SignIn::class.java)
            startActivity(intent)
            finish()

        }, 3000)
    }
}
