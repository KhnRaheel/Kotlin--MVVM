package com.example.myapplication.myapplication.activities

import android.content.Intent
import android.hardware.usb.UsbDevice.getDeviceId
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.Utils.LmaUtils
import com.example.myapplication.myapplication.Utils.ShaedPreferences

import com.example.myapplication.myapplication.data.Constants
import com.example.myapplication.myapplication.data.MusicDataBase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class LogInActivity : AppCompatActivity()  {
    lateinit var email:String
    lateinit var password:String
    var auth= FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()
    val tag="tag"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        if (auth.currentUser != null) {
           startActivity(Intent(this,DashBoardActivity::class.java))

        }
        ShaedPreferences().putStringSharedPrefs(this,LmaUtils().getDeviceId(this),Constants.KEY_CURRENT_DEVICE_IMEI)


    }

    fun signUp(view: View) {
        Intent(this, SignUpActivity::class.java).also {
            startActivity(it)
        }

    }

    fun Login(view: View) {
        initdaata()

        Log.d(tag, "emssil and log")
        initsignIn()


    }

    private fun initsignIn() {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    startActivity(Intent(this@LogInActivity, DashBoardActivity::class.java))
                    Toast.makeText(this," log in", Toast.LENGTH_SHORT).show()
                    finish()



                }
            }
        }
    }

    private fun initdaata() {
        email= et_email.text.toString()
        password=et_email.text.toString()
    }
}