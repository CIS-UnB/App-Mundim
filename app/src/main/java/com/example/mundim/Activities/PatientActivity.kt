package com.example.mundim.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.widget.TextView
import com.android.volley.Response
import com.beust.klaxon.Klaxon
import com.example.mundim.Classes.Primary.Patient
import com.example.mundim.Classes.Primary.Sample
import com.example.mundim.Fragments.AmostrasFragment
import com.example.mundim.Fragments.PatientData2Fargment
import com.example.mundim.Fragments.SampleAdapter
import com.example.mundim.R
import com.example.mundim.Services.*
import kotlinx.android.synthetic.main.activity_patient.*
import org.jetbrains.anko.toast
import org.w3c.dom.Text

class PatientAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        if (position == 0){
            return PatientData2Fargment()
        }
        return AmostrasFragment()
    }

    override fun getCount(): Int {
        return 2
    }
}

class PatientActivity : AppCompatActivity(), AmostrasFragment.SampleFragmentInterface {
    private lateinit var pageAdapter: PatientAdapter

    var samples = ArrayList<Sample>()
    lateinit var loadingTV: TextView
    lateinit var sampleAdapter: SampleAdapter
    lateinit var fragment: PatientData2Fargment

    override fun getSamplesList(): ArrayList<Sample> {
        return samples
    }

    override fun setAdapter(adapter: SampleAdapter) {
        sampleAdapter = adapter
    }

    override fun setLoadingTextView(textView: TextView) {
        loadingTV = textView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)

        val bundle:Bundle = intent.extras
        patientNameTextView.text = bundle.getString("nome")

        val listener = Response.Listener<String> { response ->
            Log.e("HttpClient", "success! response: $response")
            loadingTV.text = "Sem amostras para este paciente."
            for (item in Klaxon().parseArray<Sample>(response)!!.iterator()){
                loadingTV.text = ""
                samples.add(item)
            }
            sampleAdapter?.notifyDataSetChanged()
        }
        val patient_id = bundle.getString("db_id")
        query("""
            SELECT samples.*, patients.nome
            FROM samples
            JOIN patients
            ON samples.patient_id = patients.db_id
            WHERE patients.db_id = $patient_id
            """.trimIndent(), this, listener)

        pageAdapter = PatientAdapter(supportFragmentManager)
        viewPager2.adapter = pageAdapter
        viewPager2!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout2))
        tabLayout2!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        backBtn2.setOnClickListener{
            onBackPressed()
        }
        trashBtn2.setOnClickListener {
            val patient_id = bundle.getString("db_id")
            query("""
            DELETE FROM `patients`
            WHERE `db_id`=$patient_id
            """.trimIndent(), this, Response.Listener {response ->  })
            var intent = Intent()
            intent.putExtra("deleted", true)
            intent.putExtra("position", bundle.getString("position"))
            setResult(2, intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_NEW_SAMPLE_DATA) {
            if (resultCode == 1) {
                samples.add(
                    Sample(
                        id = data!!.extras.getString("id"),
                        patient_id = data!!.extras.getString("patient_id"),
                        area_anatomica = data!!.extras.getString("area_anatomica"),
                        antecedentes_familiares = data!!.extras.getString("antecedentes_familiares"),
                        diagnostico_dermatologista = data!!.extras.getString("diagnostico_dermatologista"),
                        diagnostico_biopsia= data!!.extras.getString("diagnostico_biopsia"),
                        nome = data!!.extras.getString("nome"),
                        data = data!!.extras.getString("data"),
                        url = data!!.extras.getString("url")
                    )
                )
                loadingTV.text = ""
                sampleAdapter.notifyDataSetChanged()

            }
        }
        if (requestCode == GET_EDITED_SAMPLE_DATA) {
            if (resultCode == 1) {
                var deleted = data!!.extras.getInt("deleted")
                var position = data!!.extras.getString("position").toInt()
                if (deleted == 1){
                    samples.removeAt(position)
                    sampleAdapter.notifyDataSetChanged()
                    if (samples.size == 0){
                        loadingTV.text = "Sem amostras para este paciente."
                    }
                }
                else{
                    samples[position].antecedentes_familiares = data!!.extras.getString("antecedentes_familiares")
                    samples[position].area_anatomica = data!!.extras.getString("area_anatomica")
                    samples[position].diagnostico_dermatologista = data!!.extras.getString("diagnostico_dermatologista")
                    samples[position].diagnostico_biopsia = data!!.extras.getString("diagnostico_biopsia")
                    samples[position].derma_url = data!!.extras.getString("derma_url")
                }
            }
        }
    }

}
