package ie.wit.countdown.main.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import ie.wit.countdown.R
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel
import kotlinx.android.synthetic.main.activity_countdown.*
import kotlinx.android.synthetic.main.content_countdown.*


var countdown = CountdownModel()

class Countdown : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        app = this.application as CountdownApp

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_countdown)


        val initialTextViewTranslationY = textView_progress.translationY
        seekBar.progress = 1
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textView_progress.text = progress.toString()

                val translationDistance = (initialTextViewTranslationY +
                        progress * resources.getDimension(R.dimen.text_anim_step) * -1)

                textView_progress.animate().translationY(translationDistance)
                if (!fromUser)
                    textView_progress.animate().setDuration(500).rotationBy(360f)
                        .translationY(initialTextViewTranslationY)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        start.setOnClickListener {v ->
            countdown()
            submit.visibility = View.VISIBLE
            Wordforcountdown.visibility = View.VISIBLE
            answer.visibility = View.VISIBLE


            textView_progress.visibility = View.GONE
            howMany.visibility = View.GONE
            reset.visibility = View.GONE
            seekBar.visibility = View.GONE
            start.visibility = View.GONE

        }

        submit.setOnClickListener {v ->

            score()

        }


        reset.setOnClickListener {

            seekBar.progress = 1
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_countdown, menu)
        return true

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_scoreboard -> { startActivity(Intent(this, Scoreboard::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun countdown() {
        var vowelsuser : Int
        vowelsuser = seekBar.progress
        val consonant =
            ("B" + "C" + "D" + "F" + "G" + "H" + "J" + "K" + "L" + "M" + "N" + "P" + "Q" + "R" + "S" + "T" + "V" + "X" + "Z")
        val vowelstest = ("A" + "E" + "I" + "O" + "U")
        //Input to take in how many vowels a user wants
        //Code which randomly generates a string based of the vars above.
        var randomString: String = List(9 - vowelsuser) { consonant.random() }.joinToString("")
        var vowelsString: String = List(vowelsuser) { vowelstest.random() }.joinToString("")

         var printedcountdown  = "$randomString" + "$vowelsString"

        Log.v("Test","${  printedcountdown }")
        printedcountdown.toUpperCase()
        Wordforcountdown.setText(printedcountdown)
    }
    fun score() {

        //below here will be the scoring method used to be able to check if the correct letters were used in the word

      var answer = answer.text.toString().toUpperCase()
        var printedCountdown = Wordforcountdown.text.toString()
       var score = answer.count { printedCountdown.contains(it) }



        println("the word you have entered is ${answer} ")
        if (score == answer.length) {
            val congratulations = getString(R.string.Congratulations) + "${score}"
            Toast.makeText(this, congratulations, Toast.LENGTH_LONG).show()
        } else {
            val error = getString(R.string.error) + "${answer}"
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            println("You have used a letter in you're word that is not in the other word ")
        }
        app.countdownstore.create(CountdownModel(score = score , answer = answer , printedcountdown = printedCountdown))
        finish();
        startActivity(getIntent());
    }

}










