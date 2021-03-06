package com.sylvie.boardgameguide

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.sylvie.boardgameguide.databinding.ActivityMainBinding
import com.sylvie.boardgameguide.databinding.NavHeaderDrawerBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.login.LoginActivity
import com.sylvie.boardgameguide.login.UserManager
import com.sylvie.boardgameguide.util.CurrentFragmentType
import com.sylvie.boardgameguide.util.DrawerToggleType

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favorite -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalFavoriteFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_game -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalGameFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_tools -> {
                    findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalToolsFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    UserManager.userToken?.let {
                        findNavController(R.id.myNavHostFragment).navigate(
                            NavigationDirections.actionGlobalProfileFragment(
                                it
                            )
                        )
                    }
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (UserManager.userToken == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        firebaseAnalytics = Firebase.analytics

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setupNavController()
        setupBottomNav()
        setupDrawer()

    }

    private fun setupBottomNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.homeFragment -> CurrentFragmentType.HOME
                R.id.newPostFragment -> CurrentFragmentType.NEW_POST
                R.id.newEventFragment -> CurrentFragmentType.NEW_EVENT
                R.id.detailPostFragment -> CurrentFragmentType.DETAIL_POST
                R.id.detailEventFragment -> CurrentFragmentType.DETAIL_EVENT
                R.id.toolsFragment -> CurrentFragmentType.TOOLS
                R.id.eventFragment -> CurrentFragmentType.EVENT
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.favoriteFragment -> CurrentFragmentType.FAVORITE
                R.id.gameFragment -> CurrentFragmentType.GAME
                R.id.newGameFragment -> CurrentFragmentType.NEW_GAME
                R.id.gameDetailFragment -> CurrentFragmentType.DETAIL_GAME
                R.id.diceFragment -> CurrentFragmentType.DICE
                R.id.timerFragment -> CurrentFragmentType.TIMER
                R.id.bottleFragment -> CurrentFragmentType.PICKER
                else -> viewModel.currentFragmentType.value
            }
        }
    }

    private fun setupDrawer() {

        // set up toolbar
        val navController = this.findNavController(R.id.myNavHostFragment)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = null

        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        NavigationUI.setupWithNavController(binding.drawerNavView, navController)

        binding.drawerNavView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_event -> {
                    viewModel.navigation.value = 1
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_eventFragment)
                    true
                }
                R.id.nav_sign_out -> {
                    UserManager.clear()
                    Toast.makeText(this, "Sign Out", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false

        actionBarDrawerToggle = object : ActionBarDrawerToggle(

            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
        }
        val bindingNavHeader = NavHeaderDrawerBinding.inflate(
            LayoutInflater.from(this), binding.drawerNavView, false
        )

        bindingNavHeader.lifecycleOwner = this
        bindingNavHeader.viewModel = viewModel
        binding.drawerNavView.addHeaderView(bindingNavHeader.root)

        viewModel.currentDrawerToggleType.observe(this, Observer { type ->

            actionBarDrawerToggle?.isDrawerIndicatorEnabled = type.indicatorEnabled
            supportActionBar?.setDisplayHomeAsUpEnabled(!type.indicatorEnabled)
            binding.toolbar.setNavigationIcon(
                when (type) {
                    DrawerToggleType.BACK -> R.drawable.ic_back
                    else -> R.drawable.ic_drawer_menu
                }
            )

            actionBarDrawerToggle?.setToolbarNavigationClickListener {
                when (type) {
                    DrawerToggleType.BACK -> findNavController(R.id.myNavHostFragment).navigateUp()
                    else -> {
                    }
                }
            }
        })
    }


    private var firstPressedTime: Long = 0
    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (viewModel.currentFragmentType.value == CurrentFragmentType.HOME) {
                if (System.currentTimeMillis() - firstPressedTime < 2000) {
                    super.onBackPressed()
                } else {
                    Toast.makeText(this@MainActivity, "再按一次退出", Toast.LENGTH_SHORT).show()
                    firstPressedTime = System.currentTimeMillis()
                }
            } else {
                super.onBackPressed()
            }
        }
    }
}