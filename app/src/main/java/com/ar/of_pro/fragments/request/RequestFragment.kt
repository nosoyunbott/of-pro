package com.ar.of_pro.fragments.request

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.ar.of_pro.R
import com.ar.of_pro.entities.Ocupation
import com.ar.of_pro.entities.Request
import com.ar.of_pro.entities.RequestFB
import com.ar.of_pro.entities.ServiceType
import com.ar.of_pro.services.ActivityServiceApiBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    var timestamp: String = ""
    lateinit var imageUrlArray: MutableList<String>

    var serviceTypesList: List<String> = ServiceType().getList()
    lateinit var serviceTypesAdapter: ArrayAdapter<String>
    lateinit var selectedServiceType: String

    lateinit var errorPriceTextView: TextView
    lateinit var errorDateTextView: TextView
    lateinit var errorTitleTextView: TextView
    lateinit var sharedPreferences: SharedPreferences

    private val db = FirebaseFirestore.getInstance()
    private var userId = FirebaseAuth.getInstance().currentUser?.uid

    var selectedServicePosition: Int = 0
    var selectedOcupationPosition: Int = 0


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, month, dayOfMonth ->
                val selectedTimestamp = createTimestamp(year, month, dayOfMonth)
                timestamp = selectedTimestamp.toString() //VALIDAR
                edtTime.setText(formatTimestamp(selectedTimestamp))
            }, currentYear, currentMonth, currentDay
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_request, container, false)

        init()

        //Clean sharedPrefs regarding spinners
        val editor = sharedPreferences.edit()
        editor.remove("selectedService")
        editor.remove("selectedOcupation")
        editor.apply()
        setupSpinner(spnOcupation, ocupationAdapter)
        setupSpinner(spnServiceTypes, serviceTypesAdapter)

        return v
    }

    private fun init() {
        spnOcupation = v.findViewById(R.id.spnOcupations)
        spnServiceTypes = v.findViewById(R.id.spnServiceTypes)
        btnAttach = v.findViewById(R.id.btnAttach)
        btnRequest = v.findViewById(R.id.btnRequest)
        edtTitle = v.findViewById(R.id.edtTitle)
        edtPriceMax = v.findViewById(R.id.edtPriceMax)
        edtDescripcion = v.findViewById(R.id.edtDescripcion)
        errorPriceTextView = v.findViewById(R.id.errorPriceTextView)
        errorDateTextView = v.findViewById(R.id.errorDateTextView)
        errorTitleTextView = v.findViewById(R.id.errorTitleTextView)
        sharedPreferences =
            requireContext().getSharedPreferences("my_preference", Context.MODE_PRIVATE)
        edtTime = v.findViewById(R.id.edtTime)
        edtTime.setOnClickListener {
            showDatePickerDialog()
        }

        val maxLength = 120
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            if (dest.length + end - start - (dend - dstart) <= maxLength) {
                null
            } else {
                ""
            }
        }
        imageUrlArray = mutableListOf()
        edtDescripcion.filters = arrayOf(inputFilter)
    }

    override fun onStart() {
        super.onStart()

        handleSpinnersPopulation()
        setOnClickListener(btnAttach)

        btnRequest.isEnabled = false
        btnRequest.isClickable = false

        btnRequest.setOnClickListener {
            errorPriceTextView.visibility = View.GONE
            if (validateForm()) {
                val clientId = sharedPreferences.getString(
                    "clientId", ""
                )  // Retrieve the 'userType' attribute from SharedPreferences
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
                    imageUrlArray
                )

                val newDocRequest = db.collection("Requests").document()
                db.collection("Requests").document(newDocRequest.id).set(r)
                val editor = sharedPreferences.edit()
                editor.remove("selectedService")
                editor.remove("selectedOcupation")
                editor.apply()
                val action = RequestFragmentDirections.actionRequestFragmentToRequestsListFragment()
                v.findNavController().navigate(action)
            }
        }
    }

    private fun handleSpinnersPopulation() {
        val posSpnService = sharedPreferences.getInt("selectedService", 0)
        val posSpnOcupation = sharedPreferences.getInt("selectedOcupation", 0)
        if (posSpnOcupation != 0) {
            spnOcupation.setSelection(posSpnOcupation)
            selectedOcupation = ocupationList[posSpnOcupation]
        }
        if (posSpnService != 0) {
            spnServiceTypes.setSelection(posSpnService)
            selectedServiceType = serviceTypesList[posSpnOcupation]
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
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                when (spinner) {
                    spnOcupation -> {
                        selectedOcupation = ocupationList[position]
                        selectedOcupationPosition = position
                    }
                    spnServiceTypes -> {
                        selectedServiceType = serviceTypesList[position]
                        selectedServicePosition = position
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(context).setTitle("Error").setMessage("ROMPISTE TODO")
            .setCancelable(true).create()
        dialog.show()
    }

    private fun setOnClickListener(btn: Button) {
        btn.setOnClickListener() {
            val editor = sharedPreferences.edit()
            editor.putInt("selectedService", selectedServicePosition)
            editor.putInt("selectedOcupation", selectedOcupationPosition)
            editor.apply()
            val intent = Intent(Intent.ACTION_PICK)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*, video/*"
            startActivityForResult(intent, PICK_MEDIA_REQUEST)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("DATA FROM INTENT", data.toString())
        imageUrlArray = mutableListOf()

        if (requestCode == PICK_MEDIA_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val count = data.clipData!!.itemCount
                if (data != null && data.clipData != null) {


                    if (count > 3) {
                        Toast.makeText(
                            context,
                            "Has excedido el límite de selección de imágenes",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    for (i in 0 until count) {
                        val selectedMediaUri = data.clipData!!.getItemAt(i).uri
                        // Handle the selected image URI as needed
                        Log.d("SELECTED_MEDIA_URI", selectedMediaUri.toString())

                        // Convert URI to blob
                        val blob = uriToBlob(selectedMediaUri)

                        // Save blob data to a file with a unique name
                        val fileName = "photo_${System.currentTimeMillis()}.jpg"
                        requireContext().openFileOutput(fileName, Context.MODE_PRIVATE)
                            .use { output ->
                                output.write(blob)
                            }

                        // Load image from URI or blob data
                        try {
                            loadImage(selectedMediaUri, blob, count)
                            Toast.makeText(
                                context,
                                "Image uploaded successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: FileNotFoundException) {
                            Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT)
                                .show()
                            Log.e("Exception", e.toString())
                        }
                    }



                } else if (data.data != null) {
                    val selectedMediaUri = data.data!!
                    val blob = uriToBlob(selectedMediaUri!!)
                    Log.d("SELECTED MEDIA URI", selectedMediaUri.toString())

                    if (selectedMediaUri != null) {
                        requireContext().openFileOutput("foto", Context.MODE_PRIVATE).use {
                            it.write(blob)
                        }

                        try {
                            loadImage(selectedMediaUri, blob, count)
                            Toast.makeText(
                                context, "la img subio ok", Toast.LENGTH_SHORT
                            ).show()
//                            btnRequest.isEnabled = true
//                            btnRequest.isClickable = true
                        } catch (e: FileNotFoundException) {
                            Toast.makeText(
                                context, "la imagen no subio", Toast.LENGTH_SHORT
                            ).show()
                            Log.d("Exc", e.toString())
                        }
                    } else {
                        Log.d("asd", "mal")
                    }

                }

            }
        }
    }

    private fun uriToBlob(uri: Uri): ByteArray {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val byteArray = inputStream?.readBytes() ?: byteArrayOf()
        inputStream?.close()
        return byteArray
    }

    private fun loadImage(uri: Uri, blob: ByteArray, count: Int) {
//        val file = File(requireContext().filesDir, "foto")
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val contentResolver = requireContext().contentResolver
            val requestBody = RequestBody.create(
                MediaType.parse(contentResolver.getType(uri)),
                inputStream?.readBytes()
            )


            val service = ActivityServiceApiBuilder.create()
//            val requestBody = RequestBody.create(
//                MediaType.parse(requireContext().contentResolver.getType(uri)), file
//            )
            val imagePart =
                MultipartBody.Part.createFormData("image", "imagetest1.jpg", requestBody)

            service.uploadImage(imagePart).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {
                    Log.d("call", call.toString())
                    if (response.isSuccessful) {
                        try {
                            val responseData = response.body()?.string()
                            val jsonResponse = JSONObject(responseData)
                            val dataObject = jsonResponse.getJSONObject("data")
                            val imageUrll = dataObject.getString("link")
                            imageUrlArray.add(dataObject.getString("link"))
                            if(imageUrlArray.size == count){
                                btnRequest.isEnabled = true
                                btnRequest.isClickable = true
                            }
                            Log.d("image", "Image URL: $imageUrll")
                            // Handle imageUrl as needed (e.g., display it in your app)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            // Handle JSON parsing error
                        }
                    } else {
                        // Handle unsuccessful response here
                        Log.e("image", response.toString())

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

    private fun validateForm(): Boolean {
        var priceValidate = false
        var titleValidate = false
        var dateValidate = false
        if (edtPriceMax.text.isNotEmpty()) {
            if (edtPriceMax.text.toString().toFloat() < Int.MAX_VALUE) {
                errorPriceTextView.visibility = View.GONE
                priceValidate = true
            } else {
                errorPriceTextView.visibility = View.VISIBLE
            }
        } else {
            errorPriceTextView.visibility = View.VISIBLE
        }
        if (!timestamp.isNullOrBlank() && timestamp > System.currentTimeMillis().toString()) {
            errorDateTextView.visibility = View.GONE
            dateValidate = true
        } else {
            errorDateTextView.visibility = View.VISIBLE
        }
        if (edtTitle.text.isNotEmpty()){
            titleValidate = true
            errorTitleTextView.visibility = View.GONE
        }else{
            errorTitleTextView.visibility = View.VISIBLE
        }
        //        if(imageUrlArray.isNotEmpty()){
//            imageValidate = true
//        }else{
//            errorImageTextView.visibility = View.VISIBLE
//            val handler = Handler()
//            handler.postDelayed({
//                errorImageTextView.visibility = View.GONE
//            }, 2000)
//        }

        return priceValidate && dateValidate && titleValidate
    }


}