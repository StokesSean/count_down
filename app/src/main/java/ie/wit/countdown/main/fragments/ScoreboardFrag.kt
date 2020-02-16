package ie.wit.countdown.main.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import ie.wit.adapters.CountdownAdapter
import ie.wit.countdown.R
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel
import kotlinx.android.synthetic.main.fragment_scoreboard.*
import kotlinx.android.synthetic.main.fragment_scoreboard.view.*


lateinit var app: CountdownApp
class ScoreboardFrag :  Fragment()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as CountdownApp


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_scoreboard, container, false)
        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        root.recyclerView.adapter = CountdownAdapter(app.countdownstore.findAll())
        root.recyclerView.visibility = View.VISIBLE
        root.challenge.visibility = View.GONE
        root.newanswer.visibility = View.GONE
        root.save.visibility = View.GONE
        root.searchbtn.setOnClickListener(){
            var listcountdowns = app.countdownstore.findAll()
            var query =  root.search.text.toString()
            var filtered = listcountdowns.filter { it.answer == query.toUpperCase() }
            root.recyclerView.adapter = CountdownAdapter(filtered)
        }
        root.delete.setOnClickListener(){
            var listcountdowns = app.countdownstore.findAll()
            var query =  root.search.text.toString()
            var filtered = listcountdowns.filter { it.answer == query.toUpperCase() }
            app.countdownstore.delete(filtered.last())
            var key = filtered.last().printedcountdown
            val ref = FirebaseDatabase.getInstance().getReference("Countdown/$key")
            ref.removeValue()
            root.recyclerView.adapter = CountdownAdapter(filtered)
            recyclerView.adapter?.notifyDataSetChanged()
        }
        root.update.setOnClickListener(){
            var listcountdowns = app.countdownstore.findAll()
            var query =  root.search.text.toString()
            var filtered = listcountdowns.filter { it.answer == query.toUpperCase() }
            root.recyclerView.adapter = CountdownAdapter(filtered)
            recyclerView.adapter?.notifyDataSetChanged()
            root.recyclerView.visibility = View.GONE
            root.challenge.visibility = View.VISIBLE
            root.newanswer.visibility = View.VISIBLE
            root.save.visibility = View.VISIBLE
            root.challenge.text = filtered.last().printedcountdown

        }
        root.save.setOnClickListener(){
            var listcountdowns = app.countdownstore.findAll()
            var query =  root.search.text.toString()
            var filtered = listcountdowns.filter { it.answer == query.toUpperCase() }
            app.countdownstore.delete(filtered.last())
            var newanswer = root.newanswer.text.toString()
            var data = CountdownModel(
                id = filtered.last().id,
                score = filtered.last().score,
                answer = newanswer.toUpperCase(),
                printedcountdown = filtered.last().printedcountdown,
                user_email = filtered.last().user_email,
                username = filtered.last().username,
                userid = filtered.last().userid)


            var key = filtered.last().printedcountdown
            val ref = FirebaseDatabase.getInstance().getReference("Countdown/$key")
            ref.setValue(data)
                .addOnSuccessListener {
                  Log.v("update", "succesfully updated user")
                    root.recyclerView.visibility = View.VISIBLE
                    root.challenge.visibility = View.GONE
                    root.newanswer.visibility = View.GONE
                    root.save.visibility = View.GONE
                }
                .addOnFailureListener {
                }

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
