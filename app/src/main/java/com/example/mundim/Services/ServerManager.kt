package com.example.mundim.Services

import android.content.Context
import android.util.Log
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.*
import java.util.HashMap

fun query(input:String, context: Context, successfulResponseListener: Response.Listener<String>){
    val queue = Volley.newRequestQueue(context)
    val url = "http://68.183.133.221/mundim/query2.php"

    val sr = object : StringRequest(
        Request.Method.POST, url,
        successfulResponseListener,
        Response.ErrorListener { error -> Log.e("HttpClient", "error: $error") }) {
        override fun getParams(): Map<String, String> {
            val params = HashMap<String, String>()
            params["query"] = input
            return params
        }

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val params = HashMap<String, String>()
            params["Content-Type"] = "application/x-www-form-urlencoded"
            return params
        }
    }
    queue.add(sr)
}

fun execute(input:String, context: Context, successfulResponseListener: Response.Listener<String>):String{
    Log.e("HttpClient", "Input: $input")
    var result = "0"

    val queue = Volley.newRequestQueue(context)
    val url = "http://68.183.133.221/mundim/execute2.php"

    val sr = object : StringRequest(
        Request.Method.POST, url,
        successfulResponseListener,
        Response.ErrorListener { error -> Log.e("HttpClient", "error: $error") }) {
        override fun getParams(): Map<String, String> {
            val params = HashMap<String, String>()
            params["query"] = input
            return params
        }

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val params = HashMap<String, String>()
            params["Content-Type"] = "application/x-www-form-urlencoded"
            return params
        }
    }
    queue.add(sr)

    return result
}