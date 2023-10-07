package com.ar.of_pro.holders

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.entities.Request

class RequestHistoryHolder(view: View):RecyclerView.ViewHolder(view){

    val requestTitle=view.findViewById<TextView>(R.id.legendTextView)
    val requestDate=view.findViewById<TextView>(R.id.rec_dateTextView)
    val workerName=view.findViewById<TextView>(R.id.nameTextView)
    fun render(requestModel: Request){
       requestTitle.text=requestModel.requestTitle
        requestDate.text=requestModel.requestDate
        workerName.text=requestModel.workerName
    }
}