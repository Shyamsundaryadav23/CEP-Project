package com.example.myhealth.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myhealth.ViewModel.MainViewModel
import com.example.myhealth.adapter.TopDoctorAdapter2
import com.example.myhealth.databinding.ActivityTopDoctors2Binding

class TopDoctorsActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityTopDoctors2Binding
    private  var viewModel =MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTopDoctors2Binding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initTopDoctor()

    }
    private fun initTopDoctor() {
        binding.apply {
            progressBarTopDoctor.visibility= View.VISIBLE
            viewModel.doctors.observe(this@TopDoctorsActivity2, Observer{
                viewTopDoctorList.layoutManager = LinearLayoutManager(this@TopDoctorsActivity2,
                    LinearLayoutManager.VERTICAL,false)
                viewTopDoctorList.adapter= TopDoctorAdapter2(it)
                progressBarTopDoctor.visibility= View.GONE
            })
            viewModel.loadDoctors()
            backBtn.setOnClickListener { finish() }
        }
    }
}