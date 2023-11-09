package com.ar.of_pro.fragments.login

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
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.utils.SharedPrefUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserLoginFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

        }
    }

    lateinit var v: View
    lateinit var emailEdt: EditText
    lateinit var passwordEdt: EditText
    lateinit var logInButton: Button
    lateinit var errorMessageTextView: TextView
    lateinit var registerTextView: TextView
    lateinit var sharedPrefUtils: SharedPrefUtils


    private val db = FirebaseFirestore.getInstance()

    val user = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_user_login, container, false)
        emailEdt = v.findViewById(R.id.emailEdt)
        passwordEdt = v.findViewById(R.id.passwordEdt)
        logInButton = v.findViewById(R.id.logInButton)
        errorMessageTextView = v.findViewById(R.id.errorMessageTextView)
        registerTextView = v.findViewById(R.id.registerTextView)
        sharedPrefUtils = SharedPrefUtils(requireContext())
        return v
    }

    override fun onStart() {
        super.onStart()
        goToApp()
        goToSignUp()
        validateUserSession()

    }

    private fun goToApp() {
        logInButton.setOnClickListener {
            errorMessageTextView.visibility = View.GONE

            if (emailEdt.text.toString().isNotEmpty() && passwordEdt.text.toString().isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        emailEdt.text.toString(),
                        passwordEdt.text.toString()
                    )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {


                            val userEmail = emailEdt.text.toString()
                            db.collection("Users").whereEqualTo("mail", userEmail).get()
                                .addOnCompleteListener { userQueryTask ->
                                    if (userQueryTask.isSuccessful) {
                                        val userType =
                                            userQueryTask.result.documents[0].get("userType")
                                                .toString()
                                        val userId = userQueryTask.result.documents[0].id
                                        sharedPrefUtils.saveUserData(userType, userId)
                                        val action =
                                            UserLoginFragmentDirections.actionUserLoginFragmentToMainActivity(
                                                userType
                                            )
                                        v.findNavController().navigate(action)
                                    }


                                }
                        } else {
                            errorMessageTextView.visibility = View.VISIBLE
                        }


                    }

            } else {
                errorMessageTextView.visibility = View.VISIBLE
            }


        }
    }


    private fun validateUserSession() {
        if (user != null) {
            val userType = sharedPrefUtils.getFromSharedPrefs("userType") ?: ""
            val action = UserLoginFragmentDirections.actionUserLoginFragmentToMainActivity(userType)
            v.findNavController().navigate(action)
        } else {
            goToApp()
            goToSignUp()
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
        spannableString.setSpan(
            clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        registerTextView.text = spannableString
        registerTextView.movementMethod = LinkMovementMethod.getInstance()
    }
}
