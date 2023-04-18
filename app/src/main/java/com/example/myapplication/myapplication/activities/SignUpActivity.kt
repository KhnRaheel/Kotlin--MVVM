package com.example.myapplication.myapplication.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.Utils.user
import com.example.myapplication.myapplication.data.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {
    var context: Activity? = null
    lateinit var UserName:String
    lateinit var email:String
    lateinit var password:String
    lateinit var confirmpassword:String
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth=FirebaseAuth.getInstance()
        if(auth.currentUser!==null){
            startActivity(Intent(this, DashBoardActivity::class.java))
        }
    }


    private fun initdata() {
        UserName = et_user_name.text.toString()
        email= et_email.text.toString()
        password= et_pass.text.toString()
        confirmpassword=et_confirm_pass.text.toString()
    }

    @SuppressLint("LogNotTimber")
    fun signUp(view: View) {
        initdata()

        if(!confirmpassword.equals(password))
        {
            et_confirm_pass.setError("password not matched ")
            return
        }
        Toast.makeText(this@SignUpActivity,"hiiiiiii",Toast.LENGTH_LONG).show()

        if(UserName.isNotEmpty()&& password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                run {
                    if (task.isSuccessful) {
                        Log.d(Constants.TAG, "hello " )
                        updatedb()
                        Toast.makeText(this@SignUpActivity,"hiiiiiii",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, DashBoardActivity::class.java))


                    }
                    else {
                        Log.d(Constants.TAG,"not logged")

                    }
                }
            }


        }



    }

    private fun updatedb() {
        val firestore=FirebaseFirestore.getInstance()
        val userdata= firestore.collection(Constants.USER_DATA)
        val user=user(UserName,email,password,"","")
        userdata.add(user).addOnCompleteListener {task->
            run {
                   if(task.isSuccessful){

                   }
            }
        }

    }
}


