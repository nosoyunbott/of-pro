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
    lateinit var txtToMain : TextView
    val client : String = "CLIENT"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_login, container, false)
        txtToMain = v.findViewById(R.id.txtToMain)
        return v
    }

    override fun onStart() {
        super.onStart()

        goToApp()
    }

    private fun goToApp() {
        txtToMain.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToMainActivity(client)
            v.findNavController().navigate(action)
        }
    }

}