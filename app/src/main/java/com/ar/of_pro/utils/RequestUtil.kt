package com.ar.of_pro.utils

import com.ar.of_pro.entities.Request
import com.ar.of_pro.models.RequestModel

class RequestUtil {
    companion object{
        fun toRequest(r: RequestModel): Request {
            return Request(
                r.requestTitle,
                r.requestBidAmount,
                r.categoryOcupation,
                r.categoryService,
                r.description,
                r.state,
                r.date,
                r.maxCost,
                r.clientId,
                r.id,
                r.imageUrlArray as MutableList<String>
            )
        }
    }
}