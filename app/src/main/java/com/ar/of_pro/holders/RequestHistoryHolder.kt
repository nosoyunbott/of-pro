package com.ar.of_pro.holders

import android.graphics.drawable.shapes.Shape
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

    fun setDate(date: String) {
        val txt : TextView = view.findViewById(R.id.rec_dateTextView)
        txt.text = date
    }

    fun setName(name: String) {
        val txt : TextView = view.findViewById(R.id.nameTextView)
        txt.text = name
    }

    fun getCardLayout () : CardView {
        return view.findViewById(R.id.cardLayout)
    }

    fun setColorReference(state:String)
    {
        val circleView: View = view.findViewById(R.id.whiteCircleImageView)

        when (state) {
            "FINALIZADA" -> circleView.setBackgroundResource(R.drawable.red_circle_background)
            // Add more cases for other states and corresponding drawable resources
            else -> circleView.setBackgroundResource(R.drawable.green_circle_background)
        }
    }
}