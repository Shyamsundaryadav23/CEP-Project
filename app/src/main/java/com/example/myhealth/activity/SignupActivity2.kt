package com.example.myhealth.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myhealth.databinding.ActivitySignup2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity2 : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignup2Binding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignup2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phoneNumber = "+91" + binding.phoneEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val retypePassword = binding.retypePasswordEditText.text.toString().trim()

            if (validateInput(name, email, phoneNumber, password, retypePassword)) {
                signupUser(name, email, phoneNumber, password)
            }
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        phoneNumber: String,
        password: String,
        retypePassword: String
    ): Boolean {
        return when {
            name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || retypePassword.isEmpty() -> {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                false
            }
            !phoneNumber.matches(Regex("^\\+?[0-9]{10,15}$")) -> {
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                false
            }
            password != retypePassword -> {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun signupUser(name: String, email: String, phoneNumber: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid

                    if (userId != null) {
                        val userData = UserData(userId, name, email, phoneNumber, password)
                        databaseReference.child(userId).setValue(userData)
                            .addOnSuccessListener {
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()
                                firebaseUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener { profileTask ->
                                        if (profileTask.isSuccessful) {
                                            Toast.makeText(
                                                this@SignupActivity2,
                                                "User registered successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            clearFields()
                                            navigateToLogin()
                                        } else {
                                            Toast.makeText(
                                                this@SignupActivity2,
                                                "Failed to update profile: ${profileTask.exception?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            }
                            .addOnFailureListener { databaseError ->
                                Toast.makeText(
                                    this@SignupActivity2,
                                    "Failed to save user data: ${databaseError.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    Toast.makeText(
                        this@SignupActivity2,
                        "Failed to register user: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun clearFields() {
        binding.nameEditText.text.clear()
        binding.emailEditText.text.clear()
        binding.phoneEditText.text.clear()
        binding.passwordEditText.text.clear()
        binding.retypePasswordEditText.text.clear()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
