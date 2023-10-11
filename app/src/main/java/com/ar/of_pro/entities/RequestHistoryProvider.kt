package com.ar.of_pro.entities

class RequestHistoryProvider {
    companion object{
     val RequestHistoryList= listOf<Request>(
        Request("Pared pintada", 5, "Pintura", "Instalacion", "Pintar pared rosa", "PENDIENTE", "3may2023", 15000, "1"),
        Request("Arreglo de cuerito", 2, "Plomeria", "Reparacion", "Arreglar canilla cocina", "PENDIENTE", "5oct2023", 10000, "2"),
         Request("Cambio de foco", 3, "Electricista", "Instalacion", "Cambiar foco cochera", "PENDIENTE", "9nov2023", 5000, "3"),
    )
    }
}