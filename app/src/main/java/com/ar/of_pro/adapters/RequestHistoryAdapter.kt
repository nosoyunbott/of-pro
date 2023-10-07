package com.ar.of_pro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.entities.Request
import com.ar.of_pro.holders.RequestHistoryHolder

class RequestHistoryAdapter(private val requestDoneList:List<Request>):RecyclerView.Adapter<RequestHistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHistoryHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return RequestHistoryHolder(layoutInflater.inflate(R.layout.reqhis_item,parent,false))
    }

    override fun getItemCount(): Int {
        return requestDoneList.size
    }

    override fun onBindViewHolder(holder: RequestHistoryHolder, position: Int) {
       val item=requestDoneList[position]
        holder.render(item)
    }

}