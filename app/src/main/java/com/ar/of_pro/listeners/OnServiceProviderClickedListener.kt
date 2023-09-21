package com.ar.of_pro.listeners

import com.ar.of_pro.entities.ServiceProvider

interface OnServiceProviderClickedListener {
    fun onViewItemDetail(serviceProvider: ServiceProvider)
}