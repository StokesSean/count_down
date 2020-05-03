package ie.wit.countdown.main.models

interface CountdownStore {
    fun findAll() : List<CountdownModel>
    fun findById(id: Long) : CountdownModel?
    fun create(countdown: CountdownModel)
    fun delete(countdown: CountdownModel)
    fun update(countdown: CountdownModel)
    fun clear()
}