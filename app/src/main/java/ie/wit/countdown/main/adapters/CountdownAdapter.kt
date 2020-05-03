package ie.wit.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import ie.wit.countdown.R
import ie.wit.countdown.main.activities.user
import ie.wit.countdown.main.fragments.app
import ie.wit.countdown.main.models.CountdownModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_countdown.view.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

interface CountdownListener{
    fun onCountdownClick(countdown:CountdownModel)
}
class CountdownAdapter  constructor( var countdowns: ArrayList<CountdownModel>,
                                    private val listener: CountdownListener)
                                      : RecyclerView.Adapter<CountdownAdapter.MainHolder>() {

    fun removeAt(position: Int) {
        countdowns.removeAt(position)
        notifyItemRemoved(position)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_countdown,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val countdown = countdowns[holder.adapterPosition]
        holder.bind(countdown,listener)

    }
    override fun getItemCount(): Int = countdowns.size


    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(countdown: CountdownModel, listener: CountdownListener) {
            itemView.tag = countdown
            itemView.scoredis.text = countdown.score.toString()
            itemView.answer.text = countdown.answer
            itemView.printed.text = countdown.printedcountdown
            itemView.setOnClickListener { listener.onCountdownClick(countdown) }
                if (countdown.photo_url == ""){
                    val user = FirebaseAuth.getInstance().currentUser
                    Picasso.get().load(user!!.photoUrl).into(itemView.usrimage)
                }
            Picasso.get().load(countdown.photo_url).into(itemView.usrimage)
            }
        }
    }




