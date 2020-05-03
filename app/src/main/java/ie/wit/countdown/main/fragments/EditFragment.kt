import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.wit.countdown.R
import ie.wit.countdown.main.fragments.ScoreboardFrag
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel


import kotlinx.android.synthetic.main.fragment_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditFragment : Fragment(), AnkoLogger {

    lateinit var app: CountdownApp
    lateinit var root: View
    var editCountdown: CountdownModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as CountdownApp

        arguments?.let {
            editCountdown = it.getParcelable("editcountdown")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_edit, container, false)

        root.editprint.text = editCountdown?.printedcountdown
        root.newanswer.setText(editCountdown?.answer)
        root.Submitnew.setOnClickListener {
            updateCountdown(editCountdown!!.uid, editCountdown!!)
            updateUsercountdown(app.auth.currentUser!!.uid,
                editCountdown!!.uid, editCountdown!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(countdown: CountdownModel) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editdcountdown",countdown)
                }
            }
    }



    fun updateUsercountdown(userId: String, uid: String?, countdown: CountdownModel) {
        app.database.child("user-countdowns").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(countdown)
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.homeFrame, ScoreboardFrag.newInstance())
                            .addToBackStack(null)
                            .commit()

                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase countdown error : ${error.message}")
                    }
                })
    }

    fun updateCountdown(uid: String?, countdown: CountdownModel) {
        app.database.child("Countdown").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(countdown)

                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase countdown error : ${error.message}")
                    }
                })
    }
}
