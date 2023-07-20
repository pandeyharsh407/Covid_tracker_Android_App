package com.example.covidtracker

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var worldCasesTV: TextView
    private lateinit var worldRecoveredTV: TextView
    private lateinit var worldDeathsTV: TextView
    private lateinit var countryCasesTV: TextView
    private lateinit var countryRecoveredTV: TextView
    private lateinit var countryDeathsTV: TextView
    private lateinit var stateRV: RecyclerView
    private lateinit var stateRVAdapter: StateRVAdapter
    private var stateList: MutableList<StateModal> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find views by ID
        worldCasesTV = findViewById(R.id.IdTVWorldTestCases)
        worldRecoveredTV = findViewById(R.id.IdTVWorldRecovered)
        worldDeathsTV = findViewById(R.id.IdTVWorldDeaths)
        countryCasesTV = findViewById(R.id.IdTVIndiaCases)
        countryRecoveredTV = findViewById(R.id.IdTVIndiaRecovered)
        countryDeathsTV = findViewById(R.id.IdTVIndiaDeaths)
        stateRV= findViewById(R.id.idRVStates)

        // Fetch data from APIs
        getStateInfo()
        getWorldInfo()
    }

    private fun getStateInfo() {
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request =
            JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    val dataObj = response.getJSONObject("data")
                    val summaryObj = dataObj.getJSONObject("summary")

                    // Get country-level data
                    val cases: Int = summaryObj.getInt("total")
                    val recovered: Int = summaryObj.getInt("discharged")
                    val deaths: Int = summaryObj.getInt("deaths")

                    // Set country-level data in views
                    countryCasesTV.text = cases.toString()
                    countryRecoveredTV.text = recovered.toString()
                    countryDeathsTV.text = deaths.toString()

                    // Get state-level data
                    val regionalArray = dataObj.getJSONArray("regional")
                    for (i in 0 until regionalArray.length()) {
                        val regionalObj = regionalArray.getJSONObject(i)

                        // Get state-wise data
                        val stateName: String = regionalObj.getString("loc")
                        val cases: Int = regionalObj.getInt("totalConfirmed")
                        val deaths: Int = regionalObj.getInt("deaths")
                        val recovered: Int = regionalObj.getInt("discharged")

                        // Create a list of StateModal objects
                        val stateModal = StateModal(stateName, recovered, deaths, cases)
                        stateList.add(stateModal)
                    }

                    // Set up the recycler view with state-level data
                    stateRVAdapter = StateRVAdapter(stateList)
                    stateRV.layoutManager = LinearLayoutManager(this)
                    stateRV.adapter = stateRVAdapter

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
                { error ->
                    Toast.makeText(this, "Fail to Get Data", Toast.LENGTH_SHORT).show()
                })

        queue.add(request)
    }

    private fun getWorldInfo() {
        val url="https://corona.lmao.ninja/v3/covid-19/all"
        val queue= Volley.newRequestQueue(this@MainActivity)
        val request = JsonObjectRequest(Request.Method.GET,url,null,{ response->
            try {
                var worldCases: Int = response.getInt("cases")
                var worldRecovered: Int = response.getInt("recovered")
                var worldDeaths: Int = response.getInt("deaths")

                // Set world-level data in views
                worldRecoveredTV.text = worldRecovered.toString()
                worldCasesTV.text = worldCases.toString()
                worldDeathsTV.text = worldDeaths.toString()

            }
            catch (e:JSONException){
                e.printStackTrace()
            }

        }, {
                error-> Toast.makeText(this,"Fail to Get Data",Toast.LENGTH_SHORT).show()

        })
        queue.add(request)
    }
}
