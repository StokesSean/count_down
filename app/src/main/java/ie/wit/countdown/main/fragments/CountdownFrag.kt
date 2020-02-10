package ie.wit.countdown.main.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.squareup.picasso.Picasso

import ie.wit.countdown.R
import ie.wit.countdown.main.activities.Homescreen
import ie.wit.countdown.main.activities.user

import ie.wit.countdown.main.models.CountdownModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_countdown.view.*
import kotlinx.android.synthetic.main.fragment_countdown.*

import kotlinx.android.synthetic.main.fragment_countdown.view.*
import kotlinx.android.synthetic.main.fragment_countdown.view.answer
import kotlinx.android.synthetic.main.nav_header_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import ie.wit.countdown.main.main.CountdownApp as CountdownApp

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class Countdownfrag :  Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as CountdownApp

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        val root = inflater.inflate(R.layout.fragment_countdown, container, false)
        activity?.title = "Countdown"



        val initialTextViewTranslationY = root.textView_progress.translationY
           root.seekBar.progress = 1
           root.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            root.textView_progress.text = progress.toString()

            val translationDistance = (initialTextViewTranslationY +
                    progress * resources.getDimension(R.dimen.text_anim_step) * -1)

            root.textView_progress.animate().translationY(translationDistance)
            if (!fromUser)
                root.textView_progress.animate().setDuration(500).rotationBy(360f)
                    .translationY(initialTextViewTranslationY)
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }
        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    })


    root.start.setOnClickListener {v ->
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

    root.submit.setOnClickListener {v ->

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

        return root;

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


            app.countdownstore.create(CountdownModel(score = score , answer = answer , printedcountdown = printedCountdown))
            val congratulations = getString(R.string.Congratulations) + " ${score} " + "  Points!"
            Toast.makeText(activity, congratulations, Toast.LENGTH_LONG).show()




        } else {


            println("You have used a letter in you're word that is not in the other word ")
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