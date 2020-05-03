package ie.wit.countdown.main.util

import android.util.Log
import com.google.firebase.database.*
import ie.wit.adapters.CountdownAdapter
import ie.wit.countdown.main.fragments.app
import ie.wit.countdown.main.models.CountdownModel

private lateinit var database: DatabaseReference
class firebasefuncs() {
    fun firebasestuff() {
        database = FirebaseDatabase.getInstance().reference
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                app.countdownstore.clear()
                Log.d("db", "onChildAdded:" + dataSnapshot.key!!)
                for (DataSnapshot in dataSnapshot.getChildren()) {
                    val countdowninfo = CountdownModel()
                    val databasetest = DataSnapshot.getValue(countdowninfo::class.java)
                    dataSnapshot.child("/").key
                    if (databasetest != null) {
                        app.countdownstore.create(
                            CountdownModel(
                                printedcountdown = databasetest.printedcountdown,
                                answer = databasetest.answer,
                                score = databasetest.score,
                                user_email = databasetest.user_email,
                                username = databasetest.username,
                                userid = databasetest.userid,
                                photo_url = databasetest.photo_url
                            )
                        )
                        val adapter = CountdownAdapter(app.countdownstore.findAll())
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("db", "onChildAdded:" + dataSnapshot.key!!)
                for (DataSnapshot in dataSnapshot.getChildren()) {
                    val countdowninfo = CountdownModel()
                    val databasetest = DataSnapshot.getValue(countdowninfo::class.java)
                    dataSnapshot.child("/").key
                    if (databasetest != null) {
                        app.countdownstore.create(
                            CountdownModel(
                                printedcountdown = databasetest.printedcountdown,
                                answer = databasetest.answer,
                                score = databasetest.score,
                                user_email = databasetest.user_email,
                                username = databasetest.username,
                                userid = databasetest.userid,
                                photo_url = databasetest.photo_url
                            )
                        )
                        val adapter = CountdownAdapter(app.countdownstore.findAll())
                        adapter!!.notifyDataSetChanged()
                    }
                }
            }

        }
        database.addChildEventListener(childEventListener)
    }
}
