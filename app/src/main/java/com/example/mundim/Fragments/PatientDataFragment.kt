package com.example.mundim.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.example.mundim.R
import com.example.mundim.Services.execute
import com.example.mundim.Services.query
import kotlinx.android.synthetic.main.fragment_patient_data.*
import kotlinx.android.synthetic.main.fragment_patient_data.view.*
import org.jetbrains.anko.async
import java.text.SimpleDateFormat
import java.util.*

class PatientDataFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_patient_data, container, false)

        view.sendBtn.setOnClickListener {
            var sample_id = System.currentTimeMillis().toString()
            val nome = nomeTextView.text.toString()
            val id = idTextView.text.toString()
            val sexo = sexoTextView.text.toString()
            val idade = idadeTextView.text.toString()
            val procedencia = procedenciaTextView.text.toString()
            val naturalidade = naturalidadeTextView.text.toString()
            val estado = estadoTextView.text.toString()
            val data = SimpleDateFormat("dd/MM, HH:mm").format(Date())
            sample_id = sample_id + System.currentTimeMillis().toString() + ".png"

            var intent = Intent()
            intent.putExtra("nome", nome)
            intent.putExtra("id", id)
            intent.putExtra("sexo", sexo)
            intent.putExtra("idade", idade)
            intent.putExtra("procedencia", procedencia)
            intent.putExtra("naturalidade", naturalidade)
            intent.putExtra("estado", estado)
            intent.putExtra("data", data)

            val listener = Response.Listener<String> { response ->
                Log.e("HttpClient", "Patient added at id: $response")
                intent.putExtra("db_id", response)
                activity!!.setResult(1, intent)
                activity!!.finish()
            }
            val user_id = activity!!.intent.extras.getString("user_id")
            async {
                val input = """
                    INSERT INTO patients
                    (nome, id, user_id, sexo, idade, procedencia, naturalidade, estado, data)
                    VALUES
                    ('$nome', '$id', '$user_id', '$sexo', '$idade', '$procedencia', '$naturalidade', '$estado', '$data')
                """.trimIndent()
                execute(input, context!!, listener)

                /* val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, Uri.fromFile(File(activity?.getExternalFilesDir(null), PIC_FILE_NAME))
                ImageUploader()
                    .uploadImage(bitmap, activity as NewPatientActivity, sample_id) */
            }
        }

        return view
    }

    override fun onDetach() {
        super.onDetach()
    }

    interface OnFragmentInteractionListener {}

}
