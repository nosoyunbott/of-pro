package com.ar.of_pro.fragments.login

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.entities.User
import com.ar.of_pro.entities.UserType
import com.google.firebase.firestore.FirebaseFirestore

class SignupFragment : Fragment() {

    lateinit var v : View
    lateinit var nameEdt : EditText
    lateinit var lastNameEdt : EditText
    lateinit var addressEdt : EditText
    lateinit var locationEdt : EditText
    lateinit var emailEdt : EditText
    lateinit var phoneEdt : EditText


    lateinit var spnUserType : Spinner
    var userTypeList: List<String> = UserType().getList()
    lateinit var userTypeAdapter: ArrayAdapter<String>
    lateinit var selectedUserType: String

    lateinit var passwordEdt : EditText
    lateinit var registerButton : Button
    lateinit var logInTextView : TextView

    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_signup, container, false)
        nameEdt = v.findViewById(R.id.nameEdt)
        lastNameEdt = v.findViewById(R.id.lastNameEdt)
        addressEdt = v.findViewById(R.id.addressEdt)
        locationEdt = v.findViewById(R.id.locationEdt)
        emailEdt = v.findViewById(R.id.emailEdt)
        phoneEdt = v.findViewById(R.id.phoneEdt)
        spnUserType = v.findViewById(R.id.spnUserType)
        passwordEdt = v.findViewById(R.id.passwordEdt)
        registerButton = v.findViewById(R.id.registerButton)
        logInTextView = v.findViewById(R.id.logInTextView)
        return v
    }

    override fun onStart() {
        super.onStart()

        setupSpinner(spnUserType, userTypeAdapter)
        goToLogIn()
        goToLogInByTextView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        userTypeAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userTypeList)
        }

    private fun goToLogIn() {
        registerButton.setOnClickListener {
            val user = User(
                nameEdt.text.toString(),
                lastNameEdt.text.toString(),
                addressEdt.text.toString(),
                locationEdt.text.toString(),
                emailEdt.text.toString(),
                passwordEdt.text.toString(),
                phoneEdt.text.toString().toInt(),
                0.0,
                0,
                selectedUserType,
            )

            val newDocUser = db.collection("Users").document()
            db.collection("Users").document(newDocUser.id).set(user)

            val action =
                SignupFragmentDirections.actionSignupFragmentToUserLoginFragment()
            v.findNavController().navigate(action)
        }
    }

    private fun setupSpinner(spinner: Spinner, adapter: ArrayAdapter<String>) {
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (spinner) {
                    spnUserType -> selectedUserType = userTypeList[position]
                }
                if(selectedUserType == "CLIENTE"){
                    selectedUserType = "CLIENT"
                }else if(selectedUserType == "PROVEEDOR"){
                    selectedUserType = "PROVIDER"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage("ROMPISTE TODO")
            .setCancelable(true)
            .create()
        dialog.show()
    }

    private fun goToLogInByTextView() {
        val spannableString = SpannableString(logInTextView.text)

        val startIndex = logInTextView.text.indexOf("Inicia Sesión")
        val endIndex = startIndex + "Inicia Sesión".length

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action =
                    SignupFragmentDirections.actionSignupFragmentToUserLoginFragment()
                v.findNavController().navigate(action)
            }
        }
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        logInTextView.text = spannableString
        logInTextView.movementMethod = LinkMovementMethod.getInstance()
    }

}