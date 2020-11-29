package com.sylvie.boardgameguide

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sylvie.boardgameguide.databinding.ActivityMainBinding
import com.sylvie.boardgameguide.util.CurrentFragmentType

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalHomeFragment())
                return@OnNavigationItemSelectedListener true
            }
//            R.id.navigation_pins -> {
//                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCatalogFragment())
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_event -> {
//                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToCartFragment())
//                return@OnNavigationItemSelectedListener true
//            }
            R.id.navigation_tools -> {
                findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.actionGlobalToolsFragment())
                return@OnNavigationItemSelectedListener true
            }
//            R.id.navigation_profile -> {
//                when (viewModel.isLoggedIn) {
//                    true -> {
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToProfileFragment(viewModel.user.value))
//                    }
//                    false -> {
//                        findNavController(R.id.myNavHostFragment).navigate(NavigationDirections.navigateToLoginDialog())
//                        return@OnNavigationItemSelectedListener false
//                    }
//                }
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setupNavController()
        setupBottomNav()
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
                else -> viewModel.currentFragmentType.value
            }
        }
    }
}