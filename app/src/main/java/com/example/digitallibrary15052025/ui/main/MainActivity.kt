package com.example.digitallibrary15052025.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import com.example.digitallibrary15052025.R
import com.example.digitallibrary15052025.ui.main.home.HomeFragment
import com.example.digitallibrary15052025.ui.main.profile.ProfilFragment
import com.example.digitallibrary15052025.ui.register.RegisterActivity
import com.google.android.material.navigation.NavigationView

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.hide()//отключение заголовка у toolbar
        //val statusBarHeight = getStatusBarHeight()
        //myView.setPadding(0, statusBarHeight, 0, 0)
       // findViewById<FrameLayout>(R.id.topbarinactivity).setOnClickListener {
           // drawerLayout.openDrawer(GravityCompat.START)
        //}

        val navigationView = findViewById<NavigationView>(R.id.nav_view_left)//отступ от стутс-бара
        ViewCompat.setOnApplyWindowInsetsListener(navigationView) { view, insets ->
            view.setPadding(
                view.paddingLeft,
                insets.systemWindowInsetTop,  // Отступ = высота статус-бара
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        /*
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                navController = navHostFragment.navController
                */
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            navigationView.setCheckedItem(R.id.nav_view_left)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingFragment()).commit()
            R.id.nav_about -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AboutAppFragment()).commit()
            R.id.nav_archiv -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ArchivFragment()).commit()
            R.id.nav_bookmark -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BookmarkFragment()).commit()
            R.id.nav_choice -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ChoiceFragment()).commit()
            R.id.nav_import -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ImportWorkFragment()).commit()
            R.id.nav_new_work -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, NewWorkFragment()).commit()
            R.id.nav_profil -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProfilFragment()).commit()
            R.id.nav_like -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LikeFragment()).commit()
            R.id.nav_logout -> {
                // Переход в RegisterActivity при выходе
                onBackRegisterActivity()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    fun onBackRegisterActivity() {
        // Переход в RegisterActivity при выходе
        startActivity(Intent(this, RegisterActivity::class.java))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Очистка стека задач
        //startActivity(intent)
        finish() // Закрыть текущую активность
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack() // Возвращаемся к предыдущему фрагменту
        } else {
            super.onBackPressed() // Завершаем активность, если стек пуст
        }
    }
    fun getStatusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }

}