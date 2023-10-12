package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ar.of_pro.R


class RequestFragmentProccessFinishClient : Fragment() {

    lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request_proccess_finish_client, container, false)
        return v
    }

    override fun onStart() {
        super.onStart()

        val request = RequestFragmentProccessFinishClientArgs.fromBundle(requireArguments()).request

        //TODO traer todos los datos de la request linea 27 y cargarlos en el fragment desde la db @Moragues

    }

    //TODO elaborar finalizacion del contrato y pasar estado a FINALIZADO @Moragues

}