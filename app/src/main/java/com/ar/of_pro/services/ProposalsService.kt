package com.ar.of_pro.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class ProposalsService {
    companion object{
        private val db = FirebaseFirestore.getInstance()
        private val proposalsCollection = db.collection("Proposals")
        fun deleteProposalsFromRequestId(requestId: String) {
            val query = proposalsCollection.whereEqualTo("requestId", requestId)

            query.get().addOnCompleteListener { querySnapshot ->
                if (querySnapshot.isSuccessful) {
                    val result = querySnapshot.result
                    for(d in result.documents){
                        d.reference.delete()
                    }
                } else {
                    Log.d("ProposalsRetrievalError", "Error retrieving proposals: ${querySnapshot.exception}")
                }
            }
        }

    }
}