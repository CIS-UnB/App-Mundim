package com.example.mundim.Fragments

import android.app.Activity
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
import com.example.mundim.Classes.Primary.User
import com.example.mundim.R
import com.example.mundim.Services.execute
import com.example.mundim.Services.query
import com.example.mundim.Services.showToast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import org.jetbrains.anko.async

class SignUpFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        view.back.setOnClickListener {
            activity!!.viewPager.currentItem = 0
        }

        view.signUpBtn.setOnClickListener {
            val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity!!.getCurrentFocus()
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            val email = emailText.text.toString()
            val username = usernameText.text.toString()
            val password = passwordText.text.toString()
            val password2 = passwordText2.text.toString()

            passwordText.setText("")
            passwordText2.setText("")
            var failed = false
            if (email == ""){
                showToast("Entre com um email válido")
                failed = true
            }
            if (password != password2){
                showToast("Senhas não coincidem")
                failed = true
            }

            val listener = Response.Listener<String> { response ->
                activity!!.viewPager.currentItem = 0
                progressBar.visibility = View.INVISIBLE
                showToast("Cadastro bem sucedido")
            }

            if (!failed){
                progressBar.visibility = View.VISIBLE
                async {
                    val input = """
                    INSERT INTO `users`
                    (`username`, `password`, `email`)
                    VALUES ('$username', '$password', '$email')
                """.trimIndent()
                    execute(input, context!!, listener)
                }
            }

        }

        return view
    }

}
