package com.dylanmissuwe.commander

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dylanmissuwe.commander.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.net.Socket


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_connect, R.id.navigation_control
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val toolbar = findViewById<Toolbar>(R.id.toolbar3) as Toolbar
        if (MyApplication.client.isLoggedIn()){
            toolbar.visibility = View.VISIBLE;
        } else {
            toolbar.visibility = View.GONE;
        }

        val disconnect = findViewById<View>(R.id.disconnect) as TextView
        disconnect.setOnClickListener {
            MyApplication.client.Disconnect()
            toolbar.visibility = View.GONE;

            val grid = findViewById<View>(R.id.gridLayout) as GridLayout?
            grid?.let {
                for (i in 0 until it.childCount){
                    val view: View = it.getChildAt(i)
                    view.isEnabled = false
                    view.alpha = 0.5f
                }
            }

            val linearlayout = findViewById<View>(R.id.sliders) as LinearLayout?
            linearlayout?.let {
                for (i in 0 until it.childCount){
                    val view: View = it.getChildAt(i)
                    view.isEnabled = false
                    view.alpha = 0.5f
                }
            }

            val button = findViewById<View>(R.id.connect_button) as Button?
            button?.isEnabled = true

            Toast.makeText(this, "Disconnected", Toast.LENGTH_LONG).show()
        }
    }
}

class MyApplication : Application() {
    companion object Factory {
        var client: TelnetClient = TelnetClient()
    }
}