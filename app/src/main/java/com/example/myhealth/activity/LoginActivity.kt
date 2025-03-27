package com.example.myhealth.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.*
import com.example.myhealth.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS), 101)
        }


        // Add TextWatcher to detect changes in EditText
        binding.etEmailOrPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString().trim()

                if (isValidPhoneNumber(input)) {
                    binding.etPassword.visibility = android.view.View.GONE
                    binding.btnLogin.visibility = android.view.View.GONE
                    binding.btnSendOtp.visibility = android.view.View.VISIBLE
                } else if (isValidEmail(input)) {
                    binding.etPassword.visibility = android.view.View.VISIBLE
                    binding.btnLogin.visibility = android.view.View.VISIBLE
                    binding.btnSendOtp.visibility = android.view.View.GONE
                } else {
                    binding.etPassword.visibility = android.view.View.GONE
                    binding.btnLogin.visibility = android.view.View.GONE
                    binding.btnSendOtp.visibility = android.view.View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailOrPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (password.isEmpty()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            signInWithEmail(email, password)
        }

        binding.btnSendOtp.setOnClickListener {
            val phone = binding.etEmailOrPhone.text.toString().trim()
            sendOtp(phone)
        }
        binding.createAccountButton.setOnClickListener {
            val intent = Intent(this, SignupActivity2::class.java)
            startActivity(intent)
        }
        binding.forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgetActivity::class.java)
            startActivity(intent)
        }


    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(Regex("^[0-9]{10,15}$"))
    }

    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendOtp(phone: String) {
        val fullPhoneNumber = if (phone.startsWith("+")) phone else "+91$phone"  // Ensure correct format
        if (fullPhoneNumber.length < 10) {
            Toast.makeText(this, "Enter a valid phone number", Toast.LENGTH_SHORT).show()
            return
        } // Change +91 to your country code

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(fullPhoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Phone Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Authentication Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@LoginActivity, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            this@LoginActivity.verificationId = verificationId

            // Navigate to OtpActivity and pass verificationId
            val intent = Intent(this@LoginActivity, OtpActivity::class.java)
            intent.putExtra("verificationId", verificationId)
            startActivity(intent)
        }

    }
}
