package com.ar.of_pro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.entities.ProposalInformation
import com.ar.of_pro.holders.ServiceProviderHolder
import com.ar.of_pro.listeners.OnProposalInformationClickedListener

class ProposalInformationAdapter(private val providerList: MutableList<ProposalInformation>, private val onItemClick: OnProposalInformationClickedListener) : RecyclerView.Adapter<ServiceProviderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceProviderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.provider_card_element, parent, false)
        return (ServiceProviderHolder(view))
    }

    override fun getItemCount(): Int {
        return providerList.size
    }

    override fun onBindViewHolder(holder: ServiceProviderHolder, position: Int) {
        val serviceProvider = providerList[position]
        holder.setBidAmount(serviceProvider.bidAmount)
        if(serviceProvider.calificationQty>0) {
            holder.setCalification(serviceProvider.calification / serviceProvider.calificationQty)
        }else{
            holder.setCalification(serviceProvider.calification)
        }
        holder.setName(serviceProvider.name)
        holder.getCardLayout().setOnClickListener{
            onItemClick.onViewItemDetail(serviceProvider)
        }

    }

}