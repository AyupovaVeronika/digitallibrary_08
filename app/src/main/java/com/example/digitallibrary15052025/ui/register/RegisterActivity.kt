package com.example.digitallibrary15052025.ui.register

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.digitallibrary15052025.R
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.navigation.NavigationView

class RegisterActivity: AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener{
    private lateinit var navController: NavController//управляет навигацией между фрагментами в host-фрагменте

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)// вызов родительского метода
        setContentView(R.layout.activity_register)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_register) as NavHostFragment
        navController = navHostFragment.navController
        supportActionBar?.hide()//отключение бара
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.singUpFragment -> {
                if (navController.currentDestination?.id != R.id.singUpFragment) {
                    navController.navigate(R.id.action_singInFragment_to_singUpFragment)
                }
            }
            R.id.singInFragment -> {
                if (navController.currentDestination?.id != R.id.singInFragment) {
                    navController.navigate(R.id.action_singUpFragment_to_singInFragment)
                }
            }
            R.id.forgotPasswordFragment -> {
                if (navController.currentDestination?.id != R.id.forgotPasswordFragment) {
                    navController.navigate(R.id.action_singInFragment_to_forgotPasswordFragment)
                }
            }
        }
        return true
    }
}