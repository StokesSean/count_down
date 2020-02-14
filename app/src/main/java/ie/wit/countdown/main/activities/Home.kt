package ie.wit.countdown.main.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import ie.wit.countdown.R
import ie.wit.countdown.main.fragments.Countdownfrag
import ie.wit.countdown.main.fragments.ScoreboardFrag
import ie.wit.countdown.main.fragments.app
import ie.wit.countdown.main.main.CountdownApp
import ie.wit.countdown.main.models.CountdownModel
import kotlinx.android.synthetic.*

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.nav_header_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*


private lateinit var auth: FirebaseAuth

val user = FirebaseAuth.getInstance().currentUser

class Homescreen : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    lateinit var ft: FragmentTransaction
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        app = application as CountdownApp

        if (user != null) {
            nav_view.getHeaderView(0).name.text = user?.displayName
            nav_view.getHeaderView(0).email.text = user?.email

            val mylist = app.countdownstore.findAll()
            val totalscore = mylist.sumBy { it.score }

            nav_view.getHeaderView(0).totaluserscore.text = "$totalscore"

            Picasso.get().load(user?.photoUrl).into(nav_view.getHeaderView(0).usersimg)
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

        database = FirebaseDatabase.getInstance().reference
        val childEventListener = object : ChildEventListener {


            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("db", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                for (DataSnapshot in dataSnapshot.getChildren()) {

                    var countdowninfo = CountdownModel()


                    var databasetest = DataSnapshot.getValue(countdowninfo::class.java)
                    dataSnapshot.child("/").key



                    if (databasetest != null) {
                        app.countdownstore.create(
                            CountdownModel(
                                printedcountdown = databasetest.printedcountdown,
                                answer = databasetest.answer,
                                score = databasetest.score,
                                usere_email = databasetest.usere_email,
                                username = databasetest.username,
                                userid = databasetest.userid,
                                photo_url = databasetest.photo_url
                            )
                        )
                    }
                }


            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                Log.d("db", "onChildAdded:" + dataSnapshot.key!!)

                // A new comment has been added, add it to the displayed list
                for (DataSnapshot in dataSnapshot.getChildren()) {

                    var countdowninfo = CountdownModel()


                    var databasetest = DataSnapshot.getValue(countdowninfo::class.java)
                    dataSnapshot.child("/").key



                    if (databasetest != null) {
                        app.countdownstore.create(
                            CountdownModel(
                                printedcountdown = databasetest.printedcountdown,
                                answer = databasetest.answer,
                                score = databasetest.score,
                                usere_email = databasetest.usere_email,
                                username = databasetest.username,
                                userid = databasetest.userid,
                                photo_url = databasetest.photo_url
                            )
                        )
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        }
        database.addChildEventListener(childEventListener)
    }


        override fun onResume() {
            super.onResume()
            var newuser = FirebaseAuth.getInstance().currentUser
            if (newuser != null) {
                nav_view.getHeaderView(0).name.text = newuser?.displayName
                nav_view.getHeaderView(0).email.text = newuser?.email

                val mylist = app.countdownstore.findAll()
                val totalscore = mylist.sumBy { it.score }

                nav_view.getHeaderView(0).totaluserscore.text = "$totalscore"

                Picasso.get().load(newuser?.photoUrl).into(nav_view.getHeaderView(0).usersimg)
            }
        }


        override fun onCreateOptionsMenu(menu: Menu): Boolean {


            menuInflater.inflate(R.menu.menu_home, menu)
            return true


        }

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
                R.id.nav_logout -> AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        startActivity(Intent(this, SignIn::class.java))
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

