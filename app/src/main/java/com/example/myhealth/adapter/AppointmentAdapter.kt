package com.example.myhealth.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myhealth.R
import com.example.myhealth.doctorside.Appointment
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentAdapter(private val appointmentList: List<Appointment>) :
    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientName: TextView = view.findViewById(R.id.textPatientName)
        val appointmentTime: TextView = view.findViewById(R.id.textAppointmentTime)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointmentList[position]
        holder.patientName.text = appointment.patientName
        holder.appointmentTime.text = "Time: ${appointment.time}"

        holder.btnAccept.setOnClickListener {
            updateAppointmentStatus(appointment.id, "Accepted")
        }

        holder.btnCancel.setOnClickListener {
            updateAppointmentStatus(appointment.id, "Cancelled")
        }
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    private fun updateAppointmentStatus(appointmentId: String, status: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("appointments").document(appointmentId)
            .update("status", status)
            .addOnSuccessListener {
                Log.d("FirestoreUpdate", "Appointment $appointmentId updated to $status")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error updating appointment", e)
            }
    }
}
