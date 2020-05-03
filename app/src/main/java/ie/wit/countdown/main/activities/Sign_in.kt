package ie.wit.countdown.main.activities
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ie.wit.countdown.R
import ie.wit.countdown.main.fragments.app
import ie.wit.countdown.main.main.CountdownApp


val providers = arrayListOf(
    AuthUI.IdpConfig.EmailBuilder().build(),
    AuthUI.IdpConfig.GoogleBuilder().build())
var RC_SIGN_IN = 1
class SignIn : AppCompatActivity() {
    lateinit var app: CountdownApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        app = application as CountdownApp
        app.auth = FirebaseAuth.getInstance()
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser

                startActivity(Intent(this, Homescreen::class.java))
                app.database = FirebaseDatabase.getInstance().reference
                Log.v("Test", "I have just logged in with   ${user?.displayName.toString()}"
                )
            } else {
                Log.v("Test", "I have completely and utterly failed to log in")
            }
        }

    }
}