package com.example.mundim.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.example.mundim.Fragments.LoginFragment
import com.example.mundim.Fragments.SignUpFragment
import com.example.mundim.R
import kotlinx.android.synthetic.main.activity_login.*

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
    }

    override fun onBackPressed() {
        viewPager.currentItem = 0
    }

}
