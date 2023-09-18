package com.ar.of_pro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.entities.Request
import com.ar.of_pro.holders.RequestCardHolder
import com.ar.of_pro.listeners.OnViewItemClickedListener

class RequestCardAdapter(private val requestList: MutableList<Request>,  private val onItemClick: OnViewItemClickedListener) :
    RecyclerView.Adapter<RequestCardHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestCardHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.request_card_element, parent, false)
        return (RequestCardHolder(view))
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    override fun onBindViewHolder(holder: RequestCardHolder, position: Int) {
        val request = requestList[position]
        holder.setTitle(request.requestTitle)
        holder.setBidsAmount(request.requestBidsAmount)


        holder.getCardLayout().setOnClickListener{
            onItemClick.onViewItemDetail(request)
        }

    }



}