package ie.wit.countdown.main.models

interface CountdownStore {
    fun findAll() : List<CountdownModel>
    fun findById(id: Long) : CountdownModel?
    fun create(countdown: CountdownModel)
}