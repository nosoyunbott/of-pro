package com.ar.of_pro.fragments.login

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.google.firebase.auth.FirebaseAuth

class RecoverPasswordFragment : Fragment() {

    lateinit var v: View
    lateinit var recoverEmailEdt: TextView
    lateinit var recoverEmailBtn: Button
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_recover_password, container, false)
        recoverEmailEdt = v.findViewById(R.id.edtEmailRecover)
        recoverEmailBtn = v.findViewById(R.id.btnEmailRecover)
        return v
    }

    override fun onStart() {
        super.onStart()

        auth = FirebaseAuth.getInstance()

        recoverEmailBtn.setOnClickListener {
            val email = recoverEmailEdt.text.toString().trim()

            if (email.isNotEmpty() && email.isNotBlank()) {
                resetPassword(email)
            } else {
                recoverEmailEdt.error = "Ingrese su correo electrónico"
            }
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Mail enviado con éxito!", Toast.LENGTH_LONG).show()
                Handler().postDelayed(
                    {
                        val action =
                            RecoverPasswordFragmentDirections.actionRecoverPasswordFragmentToUserLoginFragment()
                        v.findNavController().navigate(action)
                    }, 2000
                )
            } else {
                Toast.makeText(
                    context,
                    "Error enviando el email de recuperación!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

}