package ie.wit.countdown.main.activities
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import ie.wit.adapters.CountdownAdapter
import ie.wit.countdown.R
import ie.wit.countdown.main.fragments.Countdownfrag
import ie.wit.countdown.main.fragments.ScoreboardFrag
import ie.wit.countdown.main.fragments.app
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_scoreboard.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

val user = FirebaseAuth.getInstance().currentUser
class Homescreen : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    lateinit var ft: FragmentTransaction
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        app = application as CountdownApp
        //Below Code sets the DisplayName,Email & Photo of the currently logged in user
        if (user != null) {
            nav_view.getHeaderView(0).name.text = user.displayName
            nav_view.getHeaderView(0).email.text = user.email
            Picasso.get().load(user.photoUrl).into(nav_view.getHeaderView(0).usersimg)
        }
        nav_view.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        ft = supportFragmentManager.beginTransaction()
        val fragment = Countdownfrag.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
        // This code was meant to update the Nav Header with the total score of the user but it doesn't work for some reason it just displays 0
            val mylist = app.countdownstore.findAll()
            val totalscore = mylist.sumBy { it.score }
            nav_view.getHeaderView(0).totaluserscore.text = "Total Score:  $totalscore"
        database = FirebaseDatabase.getInstance().reference
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var listcountdowns = app.countdownstore.findAll()
        var filtered5 = listcountdowns.filter { it.score >= 5 }
        var filtered6 = listcountdowns.filter { it.score >= 6 }
        var filtered7 = listcountdowns.filter { it.score >= 7 }
        var filtered8 = listcountdowns.filter { it.score >= 8 }
        var filtered9 = listcountdowns.filter { it.score >= 9 }
        when (item.itemId) {
            R.id.score5 -> recyclerView.adapter = CountdownAdapter(filtered5)
            R.id.score6 -> recyclerView.adapter = CountdownAdapter(filtered6)
            R.id.score7 -> recyclerView.adapter = CountdownAdapter(filtered7)
            R.id.score8 -> recyclerView.adapter = CountdownAdapter(filtered8)
            R.id.score9 -> recyclerView.adapter = CountdownAdapter(filtered9)
        }

        return super.onOptionsItemSelected(item)
        recyclerView.adapter?.notifyDataSetChanged()
    }
           override fun onResume() {
            super.onResume()
            //Below codes checks if a new user has logged in and changes the details to that.
            val newuser = FirebaseAuth.getInstance().currentUser
            if (newuser != null) {
                nav_view.getHeaderView(0).name.text = newuser.displayName
                nav_view.getHeaderView(0).email.text = newuser.email
                val mylist = app.countdownstore.findAll()
                val totalscore = mylist.sumBy { it.score }
                nav_view.getHeaderView(0).totaluserscore.text = "$totalscore"
                Picasso.get().load(newuser.photoUrl).into(nav_view.getHeaderView(0).usersimg)
            }
        }
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_scoreboard, menu)
            return true

        }

    //This function takes in the input and filters the countdown data class based on that result

        private fun navigateTo(fragment: Fragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.homeFrame, fragment)
                .addToBackStack(null)
                .commit()
        }
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.nav_donate -> navigateTo(Countdownfrag.newInstance())
                R.id.nav_report -> navigateTo(ScoreboardFrag.newInstance())
                R.id.nav_logout -> AuthUI.getInstance().signOut(this).addOnCompleteListener {startActivity(Intent(this, SignIn::class.java))
                 }
                else -> println("You Selected Something Else")
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
        override fun onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START)
            else
                super.onBackPressed()
        }
    }



