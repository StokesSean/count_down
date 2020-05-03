package ie.wit.countdown.main.fragments


import android.os.Bundle
import android.util.Log

import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ReportFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.wit.adapters.CountdownAdapter
import ie.wit.adapters.CountdownListener
import ie.wit.countdown.R
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel
import ie.wit.countdown.main.util.SwipeToDeleteCallback
import ie.wit.countdown.main.util.SwipeToEditCallback

import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_scoreboard.*
import kotlinx.android.synthetic.main.fragment_scoreboard.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info


lateinit var app: CountdownApp
lateinit var root: View
class ScoreboardFrag :  Fragment(), AnkoLogger,
    CountdownListener  {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        app = activity?.application as CountdownApp
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerView.adapter as CountdownAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                deleteCountdown((viewHolder.itemView.tag as CountdownModel).uid)
                deleteUserCountdown(app.auth.currentUser!!.uid,
                    (viewHolder.itemView.tag as CountdownModel).uid)
            }
        }
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_scoreboard, container, false)
        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        var userlist = ArrayList<CountdownModel>()
        root.recyclerView.adapter = CountdownAdapter(userlist,this@ScoreboardFrag)
        root.swipeContainer.setOnRefreshListener {
            root.recyclerView.adapter = CountdownAdapter(userlist,this@ScoreboardFrag)
            getAllCountdowns(app.auth.currentUser!!.uid)
            root.swipeContainer.isRefreshing = false
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerView)
        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onCountdownClick(viewHolder.itemView.tag as CountdownModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerView)
        return root
    }

    fun checkSwipeRefresh() {
        if (root.swipeContainer.isRefreshing) root.swipeContainer.isRefreshing = false
    }
    fun deleteUserCountdown(userId: String?, uid: String?) {
        app.database.child("user-countdowns").child(userId!!).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                       snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase  error : ${error.message}")
                    }
                }
            )
    }
    fun deleteCountdown(uid: String?) {
        app.database.child("Countdown").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase  error : ${error.message}")
                    }
                })
    }

    fun getAllCountdowns(userId: String?){
        var coundownList = ArrayList<CountdownModel>()
        app.database.child("user-countdowns").child(userId!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {

                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val children = snapshot!!.children
                    children.forEach{
                        val countdown = it.
                        getValue<CountdownModel>(CountdownModel::class.java)

                        coundownList.add(countdown!!)
                       root.recyclerView.adapter=CountdownAdapter(coundownList,this@ScoreboardFrag)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("user-countdowns").child(userId)
                            .removeEventListener(this)
                    }
                }
            })
    }
    override fun onCountdownClick(countdown: CountdownModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditFragment.newInstance(countdown))
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        getAllCountdowns(app.auth.currentUser!!.uid)
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            ScoreboardFrag().apply {
                arguments = Bundle().apply { }
            }
    }


}
