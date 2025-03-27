package com.example.myhealth.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myhealth.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ForgetActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var inputField: EditText
    private lateinit var resetButton: Button
    private var storedVerificationId: String? = null
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgetpassword)

        auth = FirebaseAuth.getInstance()
        inputField = findViewById(R.id.ResetEdittext)
        resetButton = findViewById(R.id.resetPasswordButton)

        resetButton.setOnClickListener {
            val input = inputField.text.toString().trim()
            if (input.isEmpty()) {
                Toast.makeText(this, "Please enter your email or phone number.", Toast.LENGTH_SHORT).show()
            } else {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    // Input is an email address
                    sendPasswordResetEmail(input)
                } else if (android.util.Patterns.PHONE.matcher(input).matches()) {
                    // Input is a phone number
                    sendOtp(input)
                } else {
                    Toast.makeText(this, "Invalid email or phone number format.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password reset email sent. Check your inbox.", Toast.LENGTH_SHORT).show()

                    // Force logout after password reset
                    FirebaseAuth.getInstance().signOut()

                    // Navigate to login screen
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to send password reset email: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



    private fun sendOtp(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Auto-retrieval or instant verification succeeded
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@ForgetActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId = verificationId
            resendToken = token
            // Prompt user to enter the OTP
            // You can navigate to another activity or show a dialog to enter the OTP
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Phone number verified.", Toast.LENGTH_SHORT).show()
                    // Proceed to allow the user to reset their password
                    // You can navigate to a password reset screen
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
