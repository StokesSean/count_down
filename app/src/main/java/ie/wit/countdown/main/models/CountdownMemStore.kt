package ie.wit.countdown.main.models

import android.util.Log

var lastId = 0L
internal fun getId() : Long { return lastId++ }
class CountdownMemStore : CountdownStore {
    val countdowns = ArrayList<CountdownModel>()
    override fun findAll(): List<CountdownModel> {
        return countdowns
    }
    override fun findById(id: Long): CountdownModel? {
        return countdowns.find { it.id == id  }
    }
    override fun create(countdown: CountdownModel) {
        countdown.id = getId()
        countdowns.add(countdown)
        logAll()
    }
    override fun update(countdown: CountdownModel) {
        var foundCountdown : CountdownModel? = countdowns.find { a -> a.id == countdown.id }
        if( foundCountdown != null){
            foundCountdown.answer = countdown.answer
        }
    }
    override fun delete(countdown: CountdownModel) {
        countdowns.remove(countdown)
    }
    override fun clear() {
        countdowns.clear()
    }
    fun logAll() {
        Log.v("coundown", "Countdown List")
        countdowns.forEach { Log.v("countdown", "$it") }
    }
}

