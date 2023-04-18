package com.example.myapplication.myapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.Utils.Device
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_device.*

class AddDeviceActivity : AppCompatActivity() {
    lateinit var name:String
    lateinit var model:String
    lateinit var phone:String
    lateinit var imei:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_device)

    }

     fun iniitData() {
        name=et_user_name.text.toString()
        model=et_email.text.toString()
        phone=et_phone.text.toString()
        imei=et_pass.text.toString()

    }

    fun add(view: View) {
        iniitData()
        val added = Device("",name,model,imei,phone)
        val dataref=FirebaseFirestore.getInstance().collection("Device")
        dataref.add(added).addOnCompleteListener{task->run{
            if(task.isSuccessful){
                Toast.makeText(this, "device added sucessfully", Toast.LENGTH_LONG).show()
                finish()

            }
        }}
    }
}