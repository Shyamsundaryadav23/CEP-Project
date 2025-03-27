package com.example.myhealth.doctorside.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myhealth.adapter.AppointmentAdapter
import com.example.myhealth.databinding.FragmentAppointmentDoctorBinding
import com.example.myhealth.doctorside.Appointment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorAppointmentFragment : Fragment() {

    private var _binding: FragmentAppointmentDoctorBinding? = null
    private val binding get() = _binding!!

    private lateinit var appointmentAdapter: AppointmentAdapter
    private lateinit var appointmentsList: MutableList<Appointment>
    private val db = FirebaseFirestore.getInstance() // ✅ Initialize Firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentDoctorBinding.inflate(inflater, container, false)
        val root: View = binding.root

        appointmentsList = mutableListOf() // ✅ Initialize list before using
        appointmentAdapter = AppointmentAdapter(appointmentsList)

        // ✅ Setup RecyclerView properly
        binding.recyclerViewAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAppointments.adapter = appointmentAdapter

        fetchAppointments()
        return root
    }

    private fun fetchAppointments() {
        db.collection("appointments")
            .whereEqualTo("doctorId", getCurrentDoctorId()) // ✅ Fetch only current doctor's appointments
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("FirestoreError", "Error fetching appointments", exception)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    appointmentsList.clear()
                    for (document in snapshot.documents) {
                        val appointment = document.toObject(Appointment::class.java)
                        if (appointment != null) {
                            appointmentsList.add(appointment)
                        }
                    }
                    appointmentAdapter.notifyDataSetChanged() // ✅ Update UI after data changes
                }
            }
    }

    private fun getCurrentDoctorId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
