package ie.wit.countdown.main.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.squareup.picasso.Picasso
import ie.wit.countdown.R
import ie.wit.countdown.main.fragments.Countdownfrag
import ie.wit.countdown.main.fragments.ScoreboardFrag
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


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



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




    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {


        menuInflater.inflate(R.menu.menu_home, menu)
        return true

        navheaders.name.text = user?.displayName
        navheaders.email.text = user?.email


        Picasso.get().load(user?.photoUrl).into(usersimg)
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
