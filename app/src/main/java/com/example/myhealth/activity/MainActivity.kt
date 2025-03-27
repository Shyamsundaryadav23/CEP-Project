package com.example.myhealth.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myhealth.R
import com.example.myhealth.ViewModel.MainViewModel
import com.example.myhealth.adapter.CategoryAdapter
import com.example.myhealth.adapter.TopDoctorAdapter
import com.example.myhealth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initCategroy()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        initCategroy()
        initTopDoctor()

    }

    private fun initTopDoctor() {
        binding.apply {
            progressBarTopDoctor.visibility=View.VISIBLE
            viewModel.doctors.observe(this@MainActivity, Observer{
             recyclerViewTopDoctor.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                recyclerViewTopDoctor.adapter=TopDoctorAdapter(it)
                progressBarTopDoctor.visibility=View.GONE
            })
            viewModel.loadDoctors()
            doctorListTxt.setOnClickListener { startActivity(Intent(this@MainActivity,TopDoctorsActivity2::class.java)) }
        }
    }

    private fun initCategroy() {
       binding.progressBarCategory.visibility= View.VISIBLE
        viewModel.category.observe(this) {
            binding.viewCategory.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategory.adapter = CategoryAdapter(it)
            binding.progressBarCategory.visibility = View.GONE
        }
        viewModel.loadCategory()

    }
}