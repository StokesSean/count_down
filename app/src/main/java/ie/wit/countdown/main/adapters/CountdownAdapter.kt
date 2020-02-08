package ie.wit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.countdown.R
import ie.wit.countdown.main.models.CountdownModel
import kotlinx.android.synthetic.main.card_countdown.view.*

class CountdownAdapter constructor(private var countdowns: List<CountdownModel>)
    : RecyclerView.Adapter<CountdownAdapter.MainHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(

            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_countdown,
                parent,

                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val countdown = countdowns[holder.adapterPosition]
        holder.bind(countdown)
    }

    override fun getItemCount(): Int = countdowns.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(countdown: CountdownModel) {

            itemView.scoredis.setText(countdown.score.toString())
            itemView.answer.setText(countdown.answer)
            itemView.printed.setText(countdown.printedcountdown)
        }
    }
}