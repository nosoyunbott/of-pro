package com.ar.of_pro.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R


class RequestCardHolder(v: View) : RecyclerView.ViewHolder(v) {

    private var view: View

    init {
        this.view = v
    }

    fun setTitle(title: String ) {
        val txt : TextView = view.findViewById(R.id.requestCardTitle)
        txt.text = title
    }

    fun setBidsAmount(bidAmount: Int ) {
        val txt : TextView = view.findViewById(R.id.requestCardBidsAmount)
        txt.text = bidAmount.toString()
    }
}

