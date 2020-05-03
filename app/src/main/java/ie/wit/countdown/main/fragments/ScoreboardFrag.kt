package ie.wit.countdown.main.fragments


import android.os.Bundle
import android.util.Log

import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ie.wit.adapters.CountdownAdapter
import ie.wit.countdown.R
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel
import ie.wit.countdown.main.util.firebasefuncs
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_scoreboard.*
import kotlinx.android.synthetic.main.fragment_scoreboard.view.*


lateinit var app: CountdownApp
class ScoreboardFrag :  Fragment()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        firebasefuncs().firebasestuff()
        super.onCreate(savedInstanceState)
        app = activity?.application as CountdownApp
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebasefuncs().firebasestuff()
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_scoreboard, container, false)
        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        var userlist = app.countdownstore.findAll()
        val user = FirebaseAuth.getInstance().currentUser
        root.recyclerView.adapter = CountdownAdapter(userlist.filter { it.username == user?.displayName.toString() })
        root.swipeContainer.setOnRefreshListener {
            firebasefuncs().firebasestuff()
            root.recyclerView.adapter = CountdownAdapter(userlist.filter { it.username == user?.displayName.toString() })
            root.swipeContainer.isRefreshing = false
        }
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
