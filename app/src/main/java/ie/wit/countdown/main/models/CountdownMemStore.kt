package ie.wit.countdown.main.models

import android.util.Log

var lastId = 0L

internal fun getId() : Long {
    return lastId++

}

class CountdownMemStore : CountdownStore {

    val countdowns = ArrayList<CountdownModel>()

    override fun findAll(): List<CountdownModel> {
        return countdowns
    }

    override fun findById(id: Long): CountdownModel? {
        val foundCountdown: CountdownModel? = countdowns.find { it.id == id  }
        return  foundCountdown
    }

    override fun create(countdown: CountdownModel) {
        countdown.id = getId()
        countdowns.add(countdown)
        logAll()
    }

    fun logAll() {

        Log.v("coundown", "Countdown List")
        countdowns.forEach { Log.v("countdown", "${it}") }
    }
}

