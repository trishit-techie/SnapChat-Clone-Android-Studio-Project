package com.example.snapchat

import android.R.attr.password
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    var emailEditText: EditText ? = null
    var passwordEditText: EditText ? = null
    var mAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        /*if(mAuth.currentUser!=null){   // some current user is there
            // login the user
            mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this,"Logged in successfully",Toast.LENGTH_SHORT).show()
                        switchToSnapsActivity()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this,"Invalid email/password", Toast.LENGTH_SHORT).show()

                    }


                }
        } */
    }
    fun switchToSnapsActivity(){

        var intent:Intent = Intent(this,SnapsActivity::class.java)
        startActivity(intent)

    }
    fun login(view:View){
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Logged in successfully",Toast.LENGTH_SHORT).show()
                    switchToSnapsActivity()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this,"Invalid email/password", Toast.LENGTH_SHORT).show()

                }


            }
    }
    fun signUp(view: View){
        mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign up success, update UI with the signed-in user's information
                    // Store the user information in a database
                    FirebaseDatabase.getInstance().getReference().child("users").child(task.result!!.user!!.uid).child("email").setValue(emailEditText?.text.toString())
                    Toast.makeText(this,"Account created successfully", Toast.LENGTH_SHORT).show()
                    switchToSnapsActivity()

                } else {
                    // If sign up fails, display a message to the user.
                    Toast.makeText(this,"Account already exists for this email-id", Toast.LENGTH_SHORT).show()

                }
            }
    }
}
