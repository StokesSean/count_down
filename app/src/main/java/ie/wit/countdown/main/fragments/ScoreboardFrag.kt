package ie.wit.countdown.main.fragments


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.wit.adapters.CountdownAdapter
import ie.wit.countdown.R
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel
import kotlinx.android.synthetic.main.fragment_scoreboard.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScoreboardFrag.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoreboardFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit var app: CountdownApp
class ScoreboardFrag :  Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as CountdownApp

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_scoreboard, container, false)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        root.recyclerView.adapter = CountdownAdapter(app.countdownstore.findAll())

        return root
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ScoreboardFrag().apply {
                arguments = Bundle().apply { }
            }
    }

}