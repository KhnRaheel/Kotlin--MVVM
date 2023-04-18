package com.example.myapplication.myapplication.activities

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.Utils.Device
import com.example.myapplication.myapplication.Utils.ShaedPreferences
import com.example.myapplication.myapplication.data.Constants
import com.example.myapplication.myapplication.recievers.ServiceReciever

import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_dash_board.*


class DashBoardActivity : AppCompatActivity(){
    lateinit var currentImei:String

    lateinit var stretimei:String

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        currentImei= ShaedPreferences().getSharedPrefernces(this,Constants.KEY_CURRENT_DEVICE_IMEI).toString()
        Log.d(Constants.TAG,"hellooo")
        Toast.makeText(this," in dashboard ",Toast.LENGTH_LONG).show()
        et_imei.setOnClickListener {
            btn_save.visibility = View.VISIBLE

        }
//


//        enableBroadcastReceiver()
        et_imei.setText(currentImei)
        initData()
    }

    private fun initData() {
        val id: String =Build.ID.toString()
        val name: String =Build.BRAND.toString()
        val model: String =Build.MODEL.toString()
        Device(id,name, model, stretimei,"" )
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

    fun save(view: View) {
        castString()
        ShaedPreferences().putStringSharedPrefs(this,stretimei,Constants.KEY_CURRENT_DEVICE_IMEI)
        btn_save.visibility=View.GONE

    }

    private fun castString() {
        stretimei=et_imei.text.toString()
    }

    fun tracking(view: View) {
        startActivity(Intent(this, TrackingActivity::class.java))
    }
    fun lockSettings(view: View) {
        startActivity(Intent(this,LockSettingActivity::class.java))
    }
    fun recovery(view: View) {
        startActivity(Intent(this, RecoveryActivity::class.java))
    }
    fun addDevice(view: View) {
        startActivity(Intent(this, AddDeviceActivity::class.java))
    }
    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LogInActivity::class.java))
        finishAffinity()
    }

}







