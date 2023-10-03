package com.ar.of_pro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.ar.of_pro.R
class LoginFragment : Fragment() {

    lateinit var v : View
    lateinit var txtClient : TextView
    lateinit var txtProvider : TextView
    val client : String = "CLIENT"
    val provider : String = "PROVIDER"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)
        txtClient = v.findViewById(R.id.txtClient)
        txtProvider= v.findViewById(R.id.txtProvider)
        return v
    }

    override fun onStart() {
        super.onStart()

        goToApp()
    }

    private fun goToApp() {
        txtClient.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMainActivity(client)
            v.findNavController().navigate(action)
        }
        txtProvider.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMainActivity(provider)
            v.findNavController().navigate(action)
        }
    }

}