package com.sylvie.boardgameguide

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sylvie.boardgameguide.databinding.ActivityMainBinding
import com.sylvie.boardgameguide.databinding.NavHeaderDrawerBinding
import com.sylvie.boardgameguide.ext.getVmFactory
import com.sylvie.boardgameguide.login.LoginActivity
import com.sylvie.boardgameguide.login.UserManager
import com.sylvie.boardgameguide.util.CurrentFragmentType

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favorite -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalFavoriteFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_event -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalEventFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_tools -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalToolsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalProfileFragment())
//                when (viewModel.isLoggedIn) {
//                    true -> {
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToProfileFragment(viewModel.user.value))
//                    }
//                    false -> {
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToLoginDialog())
//                        return@OnNavigationItemSelectedListener false
//                    }
//                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (UserManager.userToken == null
        ) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

//        else{
//        val userName = UserManager.userToken  ?:  "No Name"
//        viewModel.loginAndSetUser(UserManager.userToken!!, userName)
//        }



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

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //get image
//                } else {
//                    Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_SHORT).show()
//                }
//                return
//            }
//        }
//    }


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
                R.id.nav_game_all -> {
                    viewModel.navigate.value = 1
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_gameFragment)
                    true
                }
                else -> false
            }
        }

        binding.drawerLayout.fitsSystemWindows = true
        binding.drawerLayout.clipToPadding = false

        actionBarDrawerToggle = object : ActionBarDrawerToggle(

            this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }
        }.apply {
            binding.drawerLayout.addDrawerListener(this)
            syncState()
//            toolbar.setNavigationIcon(R.drawable.ic_menu)
        }
        val bindingNavHeader = NavHeaderDrawerBinding.inflate(
            LayoutInflater.from(this), binding.drawerNavView, false)

        bindingNavHeader.lifecycleOwner = this
        bindingNavHeader.viewModel = viewModel
        binding.drawerNavView.addHeaderView(bindingNavHeader.root)
    }

//    private fun checkPermission() {
//        val permission = ActivityCompat.checkSelfPermission(this@MainActivity,
//            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            //未取得權限，向使用者要求允許權限
//            ActivityCompat.requestPermissions(this, arrayOf(
//                Manifest.permission.CAMERA,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                MY_PERMISSIONS_REQUEST_READ_CONTACTS)
//        } else {
////            getLocalImg()
//        }
//    }

}