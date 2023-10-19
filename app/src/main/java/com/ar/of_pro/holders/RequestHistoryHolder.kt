package com.ar.of_pro.holders

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.entities.Request

class RequestHistoryHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var view : View

    init {
        this.view = view
    }

    fun setTitle(title: String) {
        val txt : TextView = view.findViewById(R.id.legendTextView)
        txt.text = title
    }

    fun setDate(date: Long) {
        val txt : TextView = view.findViewById(R.id.rec_dateTextView)
        txt.text = date.toString()
    }

    fun setName(name: String) {
        val txt : TextView = view.findViewById(R.id.nameTextView)
        txt.text = name
    }

    fun getCardLayout () : CardView {
        return view.findViewById(R.id.cardLayout)
    }
}