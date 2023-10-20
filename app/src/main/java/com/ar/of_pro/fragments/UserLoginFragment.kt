package com.ar.of_pro.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.google.firebase.firestore.FirebaseFirestore


class UserLoginFragment : Fragment() {

    lateinit var v: View
    lateinit var emailEdt: EditText
    lateinit var passwordEdt: EditText
    lateinit var logInButton: Button
    lateinit var errorMessageTextView: TextView
    lateinit var registerTextView: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_user_login, container, false)
        emailEdt = v.findViewById(R.id.emailEdt)
        passwordEdt = v.findViewById(R.id.passwordEdt)
        logInButton = v.findViewById(R.id.logInButton)
        errorMessageTextView = v.findViewById(R.id.errorMessageTextView)
        registerTextView = v.findViewById(R.id.registerTextView)
        return v
    }

    override fun onStart() {
        super.onStart()

        goToApp()
        goToSignUp()
    }

    private fun goToApp() {
        logInButton.setOnClickListener {
            val user = db.collection("Users").whereEqualTo("mail", emailEdt.text.toString()).get().addOnSuccessListener { users ->
                errorMessageTextView.visibility = View.GONE;
                for(user in users){
                    if(passwordEdt.text.toString() == user.getString("password").toString()) {
                        val action = UserLoginFragmentDirections.actionUserLoginFragmentToMainActivity(user.getString("userType").toString())
                        v.findNavController().navigate(action)
                    }
                    else{
                        errorMessageTextView.visibility = View.VISIBLE;
                    }
                }
            }.addOnFailureListener { Exception ->
                println("Error getting documents: $Exception")
            }
        }
    }

    private fun goToSignUp() {
        val spannableString = SpannableString(registerTextView.text)

        val startIndex = registerTextView.text.indexOf("Regístrate")
        val endIndex = startIndex + "Regístrate".length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = UserLoginFragmentDirections.actionUserLoginFragmentToSignupFragment()
                v.findNavController().navigate(action)
            }
        }
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        registerTextView.text = spannableString
        registerTextView.movementMethod = LinkMovementMethod.getInstance()
    }
}