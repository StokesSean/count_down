package ie.wit.countdown.main.main

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import ie.wit.countdown.main.models.*

import ie.wit.countdown.R

import kotlinx.android.synthetic.main.activity_main.*
var countdown= CountdownModel()


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        start.setOnClickListener { v ->
            // TO-DO CHANGE THIS TO IT'S OWN VIEW THIS STUFF IS NASTY

            submit.visibility = View.VISIBLE
            Wordforcountdown.visibility = View.VISIBLE
            answer.visibility = View.VISIBLE


            textView_progress.visibility = View.GONE
            howMany.visibility = View.GONE
            reset.visibility = View.GONE
            seekBar.visibility = View.GONE
            start.visibility = View.GONE

            countdown()
        }
            submit.setOnClickListener {v ->

                score()

            }



        reset.setOnClickListener { v ->

            seekBar.progress = 1
        }
    }


    fun countdown() {

        // Function Explained:
        // This function generates two random words, one is filled with consonants and the other is filled with vowels.
        // In countdown the maximum word is 9 therefore the user picks x amount of vowels which determines the length of the vowel word and the consanant word is 9 minus vowel.length


        // references & sources for pieces of code
        // source for this random string generator comes from   I mixed it up a decent bit                  https://stackoverflow.com/questions/46943860/idiomatic-way-to-generate-a-random-alphanumeric-string-in-kotlin/57077555
        // source for the Code which will check how many vowels are in a string                             https://www.programiz.com/kotlin-programming/examples/vowel-consonant-count-string
        // source for the Code that checks the occurrence of chars between 2 strings for the score           https://stackoverflow.com/questions/49846295/kotlin-count-occurrences-of-chararray-in-string
        // source for the slideing bar thingy https://resocoder.com/2017/11/17/make-your-first-android-app-with-kotlin-android-developer-tutorial-for-beginners-code/


        //Local Variables used to make the random string work

        var vowelsuser = 0
        vowelsuser = seekBar.progress

        val consonant =
            ("b" + "c" + "d" + "f" + "g" + "h" + "j" + "k" + "l" + "m" + "n" + "p" + "q" + "r" + "s" + "t" + "v" + "x" + "z")

        val vowelstest = ("a" + "e" + "i" + "o" + "u")

        //Input to take in how many vowels a user wants



        //Code which randomly generates a string based of the vars above.

        var randomString: String = List(9 - vowelsuser) { consonant.random() }.joinToString("")
        var vowelsString: String = List(vowelsuser) { vowelstest.random() }.joinToString("")

        // var to check how many vowels are actually in the string

        var vowels = 0

// This Code will check how many vowels were actually in the string which was generated, it will be commented out later but is here for testing purposes.

      countdown.printedcountdown = "$randomString" + "$vowelsString"


        Wordforcountdown.setText(countdown.printedcountdown)

        countdown.printedcountdown = countdown.printedcountdown.toLowerCase()
        for (i in 0..countdown.printedcountdown.length - 1) {
            val ch = countdown.printedcountdown[i]
            if (ch == 'a' || ch == 'e' || ch == 'i'
                || ch == 'o' || ch == 'u'
            ) {
                ++vowels
            }
        }

    }
        //output of this function is then printed out to the console
        fun score() {

            //below here will be the scoring method used to be able to check if the correct letters were used in the word

            countdown.answer = answer.text.toString()



            countdown.score = countdown.answer.count { countdown.printedcountdown.contains(it) }



            println("the word you have entered is ${countdown.answer} ")
            if (countdown.score == countdown.answer.length) {
                val congratulations = getString(R.string.Congratulations) + "${countdown.score}"
                Toast.makeText(this, congratulations, Toast.LENGTH_LONG).show()
            } else {
                val error = getString(R.string.error) + "${countdown.answer}"
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                println("You have used a letter in you're word that is not in the other word ")
            }

        }

}







