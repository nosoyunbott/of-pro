package com.ar.of_pro.services

import com.google.firebase.firestore.FirebaseFirestore

class UserService {
    companion object{
        private val db = FirebaseFirestore.getInstance()
        private val usersCollection = db.collection("Users")

        fun updateRatingOfUser(ratingPoint:Float,userId:String)
        {
            val userDocRef = usersCollection.document(userId)

            userDocRef.get().addOnSuccessListener { userDocument ->
                // Retrieve the current rating from the document
                val currentRating = userDocument.getDouble("rating") ?: 0.0
                val ratingQuantity= userDocument.getDouble("ratingQuantity") ?: 0.0
                val ratingQuantityUpdated=ratingQuantity+1
                // Calculate the new rating
                val newRating = currentRating + ratingPoint

                // Update the user's rating in the Firestore document
                val updates = hashMapOf<String, Any>(
                    "rating" to newRating,
                    "ratingQuantity" to ratingQuantityUpdated
                )

                userDocRef.update(updates)

            }

        }
    }
}