package ie.wit.countdown.main.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.service.textservice.SpellCheckerService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textservice.SpellCheckerInfo
import android.view.textservice.SpellCheckerSession
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import ie.wit.countdown.R
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel

import kotlinx.android.synthetic.main.fragment_countdown.*
import kotlinx.android.synthetic.main.fragment_countdown.view.*
import org.jetbrains.anko.info
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Time
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.timer
import kotlin.system.measureTimeMillis

private val handler = Handler()
class Countdownfrag :  Fragment() {
    lateinit var handler: Handler
    lateinit var app: CountdownApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as CountdownApp
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_countdown, container, false)
           activity?.title = "Countdown"
           //This code is the code for the Seekbar with the animations, See References
           val initialTextViewTranslationY = root.textView_progress.translationY
           root.seekBar.progress = 1
           root.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            root.textView_progress.text = progress.toString()
            val translationDistance = (initialTextViewTranslationY + progress * resources.getDimension(R.dimen.text_anim_step) * -1)
            root.textView_progress.animate().translationY(translationDistance)
            if (!fromUser)
                root.textView_progress.animate().setDuration(500).rotationBy(360f).translationY(initialTextViewTranslationY)
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }
        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
    })
        //Starts the scene and Generates a word
        root.start.setOnClickListener {
            countdown()
            root.submit.visibility = View.VISIBLE
            root.Wordforcountdown.visibility = View.VISIBLE
            root.answer.visibility = View.VISIBLE
            root.textView_progress.visibility = View.GONE
            root.howMany.visibility = View.GONE
            root.reset.visibility = View.GONE
            root.seekBar.visibility = View.GONE
            root.start.visibility = View.GONE
    }
        //Resets the scene and scores the answer
        root.submit.setOnClickListener {

            score()
            root.submit.visibility = View.GONE
            root.Wordforcountdown.visibility = View.GONE
            root.answer.visibility = View.GONE
            root.answer.text = null
            root.textView_progress.visibility = View.VISIBLE
            root.howMany.visibility = View.VISIBLE
            root.reset.visibility = View.VISIBLE
            root.seekBar.visibility = View.VISIBLE
            root.start.visibility = View.VISIBLE
            root.seekBar.progress = 1
    }
        root.reset.setOnClickListener {
            root.seekBar.progress = 1
    }
        return root
    }
    fun countdown() {
        //Vowelsuser is how many vowels the users wants
        val vowelsuser  = seekBar.progress
        val consonant = ("B" + "C" + "D" + "F" + "G" + "H" + "J" + "K" + "L" + "M" + "N" + "P" + "Q" + "R" + "S" + "T" + "V" + "X" + "Z")
        val vowelstest = ("A" + "E" + "I" + "O" + "U")
        val randomString: String = List(9 - vowelsuser) { consonant.random() }.joinToString("")
        val vowelsString: String = List(vowelsuser) { vowelstest.random() }.joinToString("")
        val printedcountdown  = randomString + vowelsString
        Log.v("Test", printedcountdown)
        printedcountdown.toUpperCase()
        val gamestart = getString(R.string.gamestart)
        Toast.makeText(activity, gamestart, Toast.LENGTH_LONG).show()
        Wordforcountdown.text = printedcountdown
    }
    fun score() {
        //below here will be the scoring method used to be able to check if the correct letters were used in the word
        var answer = answer.text.toString().toUpperCase()
        var printedCountdown = Wordforcountdown.text.toString()
        var score = answer.count { printedCountdown.contains(it) }
        var userinfo = FirebaseAuth.getInstance().currentUser
        println("the word you have entered is ${answer} ")
        if (score == answer.length) {
            val congratulations = getString(R.string.Congratulations) + " ${score} " + "  Points!"
            Toast.makeText(activity, congratulations, Toast.LENGTH_LONG).show()
            if (userinfo!=null) {
                writeNewCountdown(
                    CountdownModel(
                        score = score,
                        answer = answer,
                        printedcountdown = printedCountdown,
                        user_email = userinfo.email.toString(),
                        username = userinfo.displayName.toString(),
                        userid = userinfo.uid.toString(),
                        photo_url = userinfo.photoUrl.toString()

                    )
                )
            }
        } else {
            println("You have used a letter in you're word that is not in the other word ")
        }
    }
    fun writeNewCountdown(countdown: CountdownModel){

        var userinfo = FirebaseAuth.getInstance().currentUser
        if (userinfo != null) {
            val uid = userinfo.uid
            val key = app.database.child("Countdown").push().key
            if (key == null) {
                Log.v("lol","Firebase Error : Key Empty")
                return
            }
            countdown.uid = key
            val countdownValues = countdown.toMap()
            val childUpdates = HashMap<String, Any>()
            childUpdates["/Countdown/$key"] = countdownValues
            childUpdates["/user-countdowns/$uid/$key"] = countdownValues
            app.database.updateChildren(childUpdates)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            Countdownfrag().apply {
                arguments = Bundle().apply {}
            }
    }
}