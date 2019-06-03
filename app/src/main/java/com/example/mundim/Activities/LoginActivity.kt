package com.example.mundim.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.view.View
import com.example.mundim.Fragments.LoginFragment
import com.example.mundim.Fragments.SignUpFragment
import com.example.mundim.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        if (position == 0){
            return LoginFragment()
        }
        return SignUpFragment()
    }

    override fun getCount(): Int {
        return 2
    }
}
class LoginActivity : AppCompatActivity() {
    private lateinit var pageAdapter: LoginAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        pageAdapter = LoginAdapter(supportFragmentManager)
        viewPager.adapter = pageAdapter

        val sharedPref = getSharedPreferences("login", Context.MODE_PRIVATE)
        val user_id = sharedPref.getString("user_id", "-1")
        if (user_id != "-1"){
            viewPager.visibility = View.INVISIBLE
            progressBar3.visibility = View.VISIBLE
            Handler().postDelayed({
                val mainIntent = Intent(this, PatientsActivity::class.java)
                mainIntent.putExtra("user_id", user_id)
                startActivity(mainIntent)
                Log.e("BUG", "Called")
            }, 1500)
        }
    }

    override fun onPause() {
        super.onPause()
        Handler().postDelayed({
            viewPager.visibility = View.VISIBLE
            progressBar3.visibility = View.INVISIBLE
        }, 1500)
        }

    override fun onBackPressed() {
        viewPager.currentItem = 0
    }

}
