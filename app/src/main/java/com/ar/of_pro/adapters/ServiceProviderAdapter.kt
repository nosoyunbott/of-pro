package com.ar.of_pro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.entities.ServiceProvider
import com.ar.of_pro.holders.RequestCardHolder
import com.ar.of_pro.holders.ServiceProviderHolder
import com.ar.of_pro.listeners.OnServiceProviderClickedListener
import com.ar.of_pro.listeners.OnViewItemClickedListener

class ServiceProviderAdapter(private val providerList: MutableList<ServiceProvider>, private val onItemClick: OnServiceProviderClickedListener) : RecyclerView.Adapter<ServiceProviderHolder>() {
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
        holder.setCalification(serviceProvider.calification)
        holder.setName(serviceProvider.name)
        holder.getCardLayout().setOnClickListener{
            onItemClick.onViewItemDetail(serviceProvider)
        }

    }

}