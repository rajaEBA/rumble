package com.example.rumble.presentation.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.rumble.R
import com.example.rumble.databinding.ActivityMainBinding
import com.example.rumble.domain.model.ProfileInfo
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var navController: NavController
    private var user: ProfileInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, null)

        user = intent.extras?.getParcelable(KEY_PROFILE)
            ?: throw IllegalStateException("field $KEY_PROFILE missing in Intent")
        //activeSubscriptions = user?.activeSubscriptions

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.nav_host_fragment),
            null
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_account -> {
                val bundle = Bundle()
                bundle.putParcelable(KEY_USER, user)

                if (navController.currentDestination?.id == R.id.accountFragment) {
                    navController.navigate(
                        R.id.accountFragment,
                        bundle,
                        NavOptions.Builder().setPopUpTo(R.id.accountFragment, true).build()
                    )
                } else {
                    navController.navigate(R.id.accountFragment, bundle)
                }

                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    companion object {

        const val KEY_PROFILE = "Profile"
        const val KEY_USER = "User"
      //  var activeSubscriptions: List<String>? = null

        fun newIntent(context: Context, user: ProfileInfo): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(KEY_PROFILE, user)
            return intent
        }
    }
}