package com.example.covidtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StateRVAdapter(private val stateList: List<StateModal>) :
    RecyclerView.Adapter<StateRVAdapter.StateRVViewHolder>() {

    class StateRVViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stateNameTV: TextView = view.findViewById(R.id.IdTVState)
        val casesTV: TextView = view.findViewById(R.id.IdTVCases)
        val recoveredTV: TextView = view.findViewById(R.id.IdTVRecovered)
        val deathsTV: TextView = view.findViewById(R.id.IdTVDeaths)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateRVViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.state_rv_item, parent, false)
        return StateRVViewHolder(view)
    }

    override fun onBindViewHolder(holder: StateRVViewHolder, position: Int) {
        val stateModal = stateList[position]
        holder.stateNameTV.text = stateModal.state
        holder.casesTV.text = stateModal.cases.toString()
        holder.recoveredTV.text = stateModal.recovered.toString()
        holder.deathsTV.text = stateModal.deaths.toString()
    }

    override fun getItemCount(): Int {
        return stateList.size
    }
}
