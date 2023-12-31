package com.ar.of_pro.holders

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R

class ServiceProviderHolder(v: View) : RecyclerView.ViewHolder(v){
    private var view: View

    init {
        this.view = v
    }

    fun setName(name: String ) {
        val txt : TextView = view.findViewById(R.id.txtFullName)
        txt.text = name
    }

    fun setBidAmount(bidAmount: Float ) {
        val txt : TextView = view.findViewById(R.id.txtBidAmount)
        val bidFormatted = "$" + bidAmount.toString()
        txt.text = bidFormatted
    }
    fun setCalification(calification: Double) {
        val txt: TextView = view.findViewById(R.id.txtCalification)
        txt.text = String.format("%.1f", calification)
    }

    fun getCardLayout (): CardView {
        return view.findViewById(R.id.provider_card)
    }

}