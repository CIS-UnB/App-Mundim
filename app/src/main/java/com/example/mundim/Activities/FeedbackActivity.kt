package com.example.mundim.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Response
import com.example.mundim.R
import com.example.mundim.Services.execute
import com.example.mundim.Services.showToast
import kotlinx.android.synthetic.main.activity_feedback.*
import org.jetbrains.anko.async

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        backBtn.setOnClickListener {
            onBackPressed()
        }
        sendBtn.setOnClickListener {
            val listener = Response.Listener<String> { response ->
                finish()
                showToast("Agradecemos o feedback!")
            }
            val user_id = intent.extras.getString("user_id")
            val descricao = descTextView.text.toString()
            val tipo = typeTextView.text.toString()

            descTextView.setText("")
            typeTextView.setText("")
            async {
                val input = """
                    INSERT INTO `feedbacks`
                    (user_id, descricao, tipo)
                    VALUES
                    ('$user_id', '$descricao', '$tipo')
                """.trimIndent()
                execute(input, applicationContext, listener)
            }
        }
    }
}
