package com.example.myapplication.myapplication.activities

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.Utils.Device
import com.example.myapplication.myapplication.Utils.ShaedPreferences
import com.example.myapplication.myapplication.data.Constants
import com.example.myapplication.myapplication.recievers.ServiceReciever
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_dash_board.et_imei
import kotlinx.android.synthetic.main.activity_log_in.et_email
import kotlinx.android.synthetic.main.activity_recovery.*

class RecoveryActivity : AppCompatActivity() {
    lateinit var strEtPhone: String
    lateinit var strEtDeviceName: String
    lateinit var strEtModelNumber: String
    lateinit var strEtDeviceIMEI: String
    lateinit var strEtEmergencyContact: String
    lateinit var currentEmergency: String
    lateinit var currentIMEI: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery)
//        enableBroadcastReceiver()

        currentEmergency=ShaedPreferences().getSharedPrefernces(this,Constants.Key_Cuurent_Emergency).toString()
        currentIMEI= ShaedPreferences().getSharedPrefernces(this,Constants.KEY_CURRENT_DEVICE_IMEI).toString()
//        updateDb()
        initUserData()

    }

    private fun updateDb() {
        val databaseref=FirebaseFirestore.getInstance().collection("Device")
        databaseref.add(Device()).addOnCompleteListener{task->run{
            if (task.isSuccessful){
                btn_save.visibility = View.GONE
                Toast.makeText(this, "device added  sucessfully", Toast.LENGTH_LONG).show()

            }
            else{
                Toast.makeText(this, "device not added ", Toast.LENGTH_LONG).show()
            }

        }}
    }

    private fun initUserData() {

            strEtPhone=et_phone.text.toString()
            et_Imei.setText(currentIMEI)
            et_emergency.setText(currentEmergency)
            et_name.setText(Device().name)
            et_model.setText(Device().modelNumber)
            et_phone.setText(Device().phone)



        }
    private fun enableBroadcastReceiver() {
        val receiver = ComponentName(this, ServiceReciever::class.java)
        val pm = this.packageManager
        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

    }


    }



