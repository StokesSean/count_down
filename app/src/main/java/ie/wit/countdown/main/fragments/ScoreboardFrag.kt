package ie.wit.countdown.main.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.adapters.CountdownAdapter
import ie.wit.countdown.R
import ie.wit.countdown.main.main.CountdownApp
import kotlinx.android.synthetic.main.fragment_scoreboard.view.*

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
        val root = inflater.inflate(R.layout.fragment_scoreboard, container, false)
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