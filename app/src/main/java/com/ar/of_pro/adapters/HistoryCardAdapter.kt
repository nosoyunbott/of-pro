package com.ar.of_pro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.RequestHistory
import com.ar.of_pro.holders.RequestCardHolder
import com.ar.of_pro.holders.RequestHistoryHolder
import com.ar.of_pro.listeners.OnViewItemClickedListener

class HistoryCardAdapter(private val requestList: MutableList<RequestHistory>, private val onItemClick: OnViewItemClickedListener) :

    RecyclerView.Adapter<RequestHistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHistoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reqhis_item, parent, false)
        return (RequestHistoryHolder(view))
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun onBindViewHolder(holder: RequestHistoryHolder, position: Int) {
        val request = requestList[position]
        holder.setTitle(request.request!!.requestTitle)
        holder.setDate(request.request!!.date)
        holder.setName(request!!.providerName)
        holder.getCardLayout().setOnClickListener {
            onItemClick.onViewItemDetail(request.request)
    }

}
}