package com.example.mundim.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.android.volley.Response
import com.beust.klaxon.Klaxon
import com.example.mundim.Activities.PatientsActivity
import com.example.mundim.Classes.Primary.Patient
import com.example.mundim.Classes.Primary.User
import com.example.mundim.R
import com.example.mundim.Services.execute
import com.example.mundim.Services.query
import com.example.mundim.Services.showToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import org.jetbrains.anko.async
import android.app.Activity
import android.support.v4.content.ContextCompat.getSystemService


class LoginFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.mundim.R.layout.fragment_login, container, false)
        view.signUpBtn.setOnClickListener {
            activity!!.viewPager.currentItem = 1
        }
        view.loginBtn.setOnClickListener {
            val listener = Response.Listener<String> { response ->
                Log.e("LoginResponse", response)
                var logged = false
                for (item in Klaxon().parseArray<User>(response)!!.iterator()){
                    val intent = Intent(context, PatientsActivity::class.java)
                    intent.putExtra("user_id", item.id)
                    activity!!.startActivity(intent)
                    logged = true
                }
                passwordText.setText("")
                if (!logged){
                    showToast("Usuário ou senha inválidos")
                }

                progressBar2.visibility = View.INVISIBLE
            }
            val username = usernameText.text.toString()
            val password = passwordText.text.toString()
            val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity!!.getCurrentFocus()
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            progressBar2.visibility = View.VISIBLE
            async {
                val input = """
                    SELECT * FROM users
                    WHERE username='$username' AND password='$password'
                """.trimIndent()
                query(input, context!!, listener)
            }
        }
        return view
    }

}
