package ie.wit.countdown.main.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.adapters.CountdownAdapter
import ie.wit.countdown.R
import ie.wit.countdown.main.main.CountdownApp
import kotlinx.android.synthetic.main.activity_scoreboard.*


lateinit var app: CountdownApp
class Scoreboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        app = this.application as CountdownApp

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scoreboard)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        recyclerView.adapter = CountdownAdapter(app.countdownstore.findAll())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scoreboard, menu)
        return true
    }
override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
        R.id.action_countdown -> { startActivity(Intent(this, Countdown::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
}
