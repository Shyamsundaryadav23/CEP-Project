package com.example.myhealth.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.myhealth.activity.DetailActivity2
import com.example.myhealth.databinding.ViewholderTopDoctorBinding
import com.example.myhealth.domain.DoctorModel

class TopDoctorAdapter (val items:MutableList<DoctorModel>): RecyclerView.Adapter<TopDoctorAdapter.Viewholder>(){
    private var context:Context? = null
    class Viewholder (val binding : ViewholderTopDoctorBinding):
            RecyclerView.ViewHolder(binding.root)
    {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopDoctorAdapter.Viewholder {
       context = parent.context
        val binding = ViewholderTopDoctorBinding.inflate(LayoutInflater.from(context),parent,false)
        return  Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.binding.nametxt.text = items[position].Name
        holder.binding.specialtxt.text = items[position].Special
        holder.binding.scoretxt.text = items[position].Rating.toString()
        holder.binding.yeartxt.text = items[position].Expriense.toString()+ " Year"
        Glide.with(holder.itemView.context)
            .load(items[position].Picture)
            .apply {RequestOptions().transform(CenterCrop())}
            .into(holder.binding.img)
        holder.itemView.setOnClickListener {
            val intent = Intent(context,DetailActivity2::class.java)
            intent.putExtra("object",items[position])
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size

}
