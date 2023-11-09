package com.ar.of_pro.services

import android.util.Log
import com.ar.of_pro.models.ProposalModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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
        suspend fun getProposalsByRequestId(requestId: String): List<ProposalModel> {
            return try {
                val querySnapshot = proposalsCollection.get().await()

                val proposalsList = mutableListOf<ProposalModel>()

                for (document in querySnapshot) {
                    val proposal = document.toObject(ProposalModel::class.java)
                    if (proposal.requestId == requestId) {
                        proposalsList.add(proposal)
                    }
                }

                return proposalsList

            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }


    }
}