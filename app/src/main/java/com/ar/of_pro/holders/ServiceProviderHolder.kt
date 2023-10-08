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
        val txt : TextView = view.findViewById(R.id.txtName)
        txt.text = name
    }

    fun setBidAmount(bidAmount: Float ) {
        val txt : TextView = view.findViewById(R.id.txtBidAmount)
        txt.text = bidAmount.toString()
    }
    fun setCalification(calification: Float) {
        val txt: TextView = view.findViewById(R.id.txtCalification)
        txt.text = calification.toString()
    }

    fun getCardLayout (): CardView {
        return view.findViewById(R.id.provider_card)
    }

}