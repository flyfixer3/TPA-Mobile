package com.tpa.questapp

import android.view.View
import com.tpa.questapp.model.Ticket

interface RecycleViewClickListener {
    // method yang akan dipanggil di MainActivity
    fun onItemClicked(view: View, ticket: Ticket)
}