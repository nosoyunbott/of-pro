package com.ar.of_pro.fragments

import android.content.SharedPreferences
import android.content.Context
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
    // Define your SharedPreferences variable
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)
        txtClient = v.findViewById(R.id.txtClient)
        txtProvider= v.findViewById(R.id.txtProvider)
        // Initialize SharedPreferences in onCreateView
        sharedPreferences = requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        return v
    }

    override fun onStart() {
        super.onStart()

        goToApp()
    }

    private fun goToApp() {
        txtClient.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMainActivity(client)
            //SharedPreference set for client
            //DUDA: Es necesario setear el atributo userType del objeto User?
            val editor = sharedPreferences.edit()
            editor.putString("userType", "client")
            editor.apply()

            v.findNavController().navigate(action)
        }
        txtProvider.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMainActivity(provider)
            val editor = sharedPreferences.edit()
            editor.putString("userType", "provider")
            editor.apply()
            //DUDA: Es necesario setear el atributo userType del objeto User?
            //SharedPreference set for provider
            v.findNavController().navigate(action)
        }
    }

}