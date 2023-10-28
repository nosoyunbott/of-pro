package com.ar.of_pro.fragments.request

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.entities.Ocupation
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.RequestFB
import com.ar.of_pro.entities.ServiceType
import com.ar.of_pro.services.ActivityServiceApiBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class RequestFragment<OutputStream> : Fragment() {



    lateinit var v: View
    lateinit var spnOcupation: Spinner
    lateinit var spnServiceTypes: Spinner
    lateinit var btnAttach: Button
    lateinit var btnRequest: Button
    private val PICK_MEDIA_REQUEST = 1

    lateinit var edtTitle: EditText
    lateinit var edtPriceMax: EditText
    lateinit var edtDescripcion: EditText
    lateinit var edtTime: EditText

    var ocupationList: List<String> = Ocupation().getList()
    lateinit var ocupationAdapter: ArrayAdapter<String>
    lateinit var selectedOcupation: String
    lateinit var timestamp: String
    lateinit var imageUrl: String

    var serviceTypesList: List<String> = ServiceType().getList()
    lateinit var serviceTypesAdapter: ArrayAdapter<String>
    lateinit var selectedServiceType: String

    private val db = FirebaseFirestore.getInstance()
    private var userId = FirebaseAuth.getInstance().currentUser?.uid
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedTimestamp = createTimestamp(year, month, dayOfMonth)
                timestamp = selectedTimestamp.toString() //VALIDAR
                edtTime.setText(formatTimestamp(selectedTimestamp))
            },
            currentYear,
            currentMonth,
            currentDay
        )
        datePickerDialog.show()
    }

    private fun createTimestamp(year: Int, month: Int, day: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.timeInMillis
    }

    private fun formatTimestamp(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_request, container, false)
        spnOcupation = v.findViewById(R.id.spnOcupations)
        spnServiceTypes = v.findViewById(R.id.spnServiceTypes)
        btnAttach = v.findViewById(R.id.btnAttach)
        btnRequest = v.findViewById(R.id.btnRequest)
        edtTitle = v.findViewById(R.id.edtTitle)
        edtPriceMax = v.findViewById(R.id.edtPriceMax)
        edtDescripcion = v.findViewById(R.id.edtDescripcion)
        edtTime = v.findViewById(R.id.edtTime)
        edtTime.setOnClickListener{
            showDatePickerDialog()
        }
        setupSpinner(spnOcupation, ocupationAdapter)
        setupSpinner(spnServiceTypes, serviceTypesAdapter)
        return v
    }

    override fun onStart() {
        super.onStart()

        val filename = "myfile"
        val fileContents = "Hello world!"
        requireContext().openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }

        setupSpinner(spnOcupation, ocupationAdapter)
        setupSpinner(spnServiceTypes, serviceTypesAdapter)
        setOnClickListener(btnAttach)


        btnRequest.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
            val clientId = sharedPreferences.getString("clientId", "")  // Retrieve the 'userType' attribute from SharedPreferences

            val title = edtTitle.text.toString()

            val r = RequestFB(
                title,
                0,
                selectedOcupation,
                selectedServiceType,
                edtDescripcion.text.toString(),
                Request.PENDING,
                timestamp, //TODO cambiar el string a su tipo correspondiente
                edtPriceMax.text.toString().toIntOrNull(),
                clientId,
                imageUrl
            )

            val newDocRequest = db.collection("Requests").document()
            db.collection("Requests").document(newDocRequest.id).set(r)
            //val action = RequestFragmentDirections.actionRequestFragmentToRequestsListFragment()
            //v.findNavController().navigate(action)
            v.findNavController().popBackStack()


        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        ocupationAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ocupationList)
        serviceTypesAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, serviceTypesList)
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
                    spnOcupation -> selectedOcupation = ocupationList[position]
                    spnServiceTypes -> selectedServiceType = serviceTypesList[position]
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

    private fun setOnClickListener(btn: Button) {
        btn.setOnClickListener() {

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*, video/*"
            startActivityForResult(intent, PICK_MEDIA_REQUEST)


        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_MEDIA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedMediaUri = data.data
                val blob = uriToBlob(selectedMediaUri!!)

                if (selectedMediaUri != null) {
                    requireContext().openFileOutput("foto", Context.MODE_PRIVATE).use {
                        it.write(blob)
                    }
                } else {
                    Log.d("asd", "mal")
                }
                try {
                    loadImage(selectedMediaUri, blob)
                    Toast.makeText(
                        context,
                        "la img subio ok",
                        Toast.LENGTH_SHORT
                    ).show()

                }catch (e: FileNotFoundException){
                    Toast.makeText(
                        context,
                        "la imagen no subio",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("Exc", e.toString())
                }
            }
        }
    }
    fun uriToBlob(uri: Uri): ByteArray {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes() ?: byteArrayOf()
        inputStream?.close()
        return byteArray
    }

    fun loadImage(uri: Uri, blob: ByteArray) {
        val file = File(requireContext().filesDir, "foto")
        try {

            val service = ActivityServiceApiBuilder.create()
            val requestBody = RequestBody.create(MediaType.parse(requireContext().contentResolver.getType(uri)), file)
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)

            service.uploadImage(imagePart).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        try {
                            val responseData = response.body()?.string()
                            val jsonResponse = JSONObject(responseData)
                            val dataObject = jsonResponse.getJSONObject("data")
                            val imageUrll = dataObject.getString("link")
                            imageUrl = dataObject.getString("link")
                            Log.d("image", "Image URL: $imageUrll")
                            // Handle imageUrl as needed (e.g., display it in your app)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            // Handle JSON parsing error
                        }
                    } else {
                        // Handle unsuccessful response here
                        Log.e("image", "Upload failed")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    // Handle network errors or errors in the server
                    Log.e("image", "Error: " + t.message)
                }
            })

        } catch (e: IOException) {
            e.printStackTrace()
            // Handle file IO exception
        }
    }



}