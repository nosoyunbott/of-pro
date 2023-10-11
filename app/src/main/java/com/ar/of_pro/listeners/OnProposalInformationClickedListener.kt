package com.ar.of_pro.listeners

import com.ar.of_pro.entities.ProposalInformation

interface OnProposalInformationClickedListener {
    fun onViewItemDetail(proposalInformation: ProposalInformation)
}