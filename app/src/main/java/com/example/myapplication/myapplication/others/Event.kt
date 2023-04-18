package com.example.myapplication.myapplication.others

open class Event<out T>(private val data: T) {
    var hasBeenHandled=true
        private  set
    fun getContentIfNotHandled(): T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled=true
            data
        }
    }
    fun peekContent()= data

}