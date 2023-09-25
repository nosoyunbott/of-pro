package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ar.of_pro.R
import com.ar.of_pro.adapters.ServiceProviderAdapter
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.ServiceProvider
import com.ar.of_pro.listeners.OnServiceProviderClickedListener
import com.ar.of_pro.listeners.OnViewItemClickedListener
import com.google.android.material.snackbar.Snackbar


class ProviderRequestsFragment : Fragment(), OnServiceProviderClickedListener {

    lateinit var v: View
    lateinit var recProviderList: RecyclerView
    var providerList: MutableList<ServiceProvider> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var serviceProviderAdapter: ServiceProviderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_provider_requests, container, false)
        recProviderList = v.findViewById(R.id.rec_providers)
        return v
    }

    override fun onStart() {
        super.onStart()
        for(i in 1..10){
            providerList.add(ServiceProvider("Gladys", 30f, 4.5))
            providerList.add(ServiceProvider("Mirta", 15f, 3.3))
            providerList.add(ServiceProvider("Gladys", 30f, 5.0))
        }

        recProviderList.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recProviderList.layoutManager = linearLayoutManager
        serviceProviderAdapter = ServiceProviderAdapter(providerList, this)
        recProviderList.adapter = serviceProviderAdapter

    }

    override fun onViewItemDetail(serviceProvider: ServiceProvider) {
        val action = ProviderRequestsFragmentDirections.actionProviderRequestsFragmentToRequestDetailFragment(serviceProvider)
        v.findNavController().navigate(action)
        Snackbar.make(v, serviceProvider.name, Snackbar.LENGTH_SHORT).show()
    }


}