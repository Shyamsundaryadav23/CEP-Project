package com.example.myhealth.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myhealth.domain.CategoryModel
import com.example.myhealth.domain.DoctorModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainViewModel():ViewModel() {
   private  val firebaseDatabase=FirebaseDatabase.getInstance()
    private  val _category = MutableLiveData<MutableList<CategoryModel>>()
    private  val _doctors = MutableLiveData<MutableList<DoctorModel>>()

    val category:LiveData<MutableList<CategoryModel>> = _category
    val doctors:LiveData<MutableList<DoctorModel>> = _doctors

    fun loadCategory()
    {
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               val lists = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children){
                    val list = childSnapshot.getValue(CategoryModel::class.java)
                    if(list!=null)
                    {
                        lists.add(list)
                    }
                }
                _category.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Database operation cancelled: ${error.message}")
            }

        })
    }
    fun loadDoctors()
    {
        val ref = firebaseDatabase.getReference("Doctors")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              val lists = mutableListOf<DoctorModel>()
                for (childSnapshot in snapshot.children)
                {
                   val list = childSnapshot.getValue(DoctorModel::class.java)
                   if (list!= null)
                   {
                       lists.add(list)
                   }
                }
                _doctors.value=lists
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
            
        })
    }
    

}