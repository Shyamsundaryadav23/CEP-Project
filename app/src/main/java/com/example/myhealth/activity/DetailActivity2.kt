package com.example.myhealth.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.myhealth.R
import com.example.myhealth.databinding.ActivityDetail2Binding
import com.example.myhealth.domain.DoctorModel

class DetailActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityDetail2Binding
    private lateinit var item:DoctorModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail2)
        binding=ActivityDetail2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun getBundle(){
        item=intent.getParcelableExtra("object")!!
        binding.apply {
            titleTxt.text=item.Name
            specialTxt.text=item.Special
            patiensTxt.text=item.Patiens
            bioTxt.text=item.Biography
            addressTxt.text=item.Address
            experiensTxt.text=item.Expriense.toString() + " Years "
            ratingTxt.text="${item.Rating}"
            backBtn.setOnClickListener {
                finish()
            }
            websiteImg.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse(item.Site))
                startActivity(i)
            }
           messageImg.setOnClickListener {
               val uri = Uri.parse("smsto:${item.Mobile}")
               val intent = Intent(Intent.ACTION_SENDTO,uri)
               intent.putExtra("sms_body","the SMS text")
               startActivity(intent)
           }
            callImg.setOnClickListener {
                val uri="tel:"+item.Mobile.trim()
                val intent = Intent(Intent.ACTION_DIAL,Uri.parse(uri))
                startActivity(intent)
            }
            directionImg.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW,Uri.parse(item.Location))
                startActivity(intent)
            }
            shareImg.setOnClickListener {
                val intent=Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_SUBJECT,item.Name)
                intent.putExtra(Intent.EXTRA_TEXT,item.Name+" "+item.Address+" "+item.Mobile)
                startActivity(Intent.createChooser(intent,"Choose one"))
            }
            Glide.with(this@DetailActivity2).load(item.Picture).into(profileImg)

        }
    }
}