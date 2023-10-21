package com.ar.of_pro.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.ar.of_pro.R


class CustomerProfileFragment : Fragment() {

    lateinit var v: View
    lateinit var btnEditCustomerProfile: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_customer_profile, container, false)
    }


    //override fun onStart() {
     //   super.onStart()
      //  btnEditCustomerProfile.setOnClickListener{

            //val action = ProviderProfileFragmentDirections.actionProviderProfileFragmentToProviderProfileEditFragment()
      //      v.findNavController().navigate(action)
  // }
  //  }

}