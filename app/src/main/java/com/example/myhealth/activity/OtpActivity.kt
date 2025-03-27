package com.example.myhealth.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myhealth.databinding.ActivityOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Get verification ID from Intent
        verificationId = intent.getStringExtra("verificationId")

        binding.verifyOtpButton.setOnClickListener {
            val otpCode = binding.otpEditText.text.toString().trim()

            if (otpCode.isEmpty() || otpCode.length < 6) {
                Toast.makeText(this, "Enter valid OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            verificationId?.let {
                val credential = PhoneAuthProvider.getCredential(it, otpCode)
                signInWithPhoneAuthCredential(credential)
            } ?: Toast.makeText(this, "Invalid verification ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "OTP Verified! Login Successful", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this, "OTP Verification Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
