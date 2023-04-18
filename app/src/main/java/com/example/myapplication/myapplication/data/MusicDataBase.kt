package com.example.myapplication.myapplication.data

import com.example.myapplication.myapplication.data.Constants.SONG_COLLECTION
import com.example.myapplication.myapplication.data.Constants.USER_DATA
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MusicDataBase {
    private val firestore= FirebaseFirestore.getInstance()
    private val SongCollection= firestore.collection(SONG_COLLECTION)



    suspend fun  getAllSongs():List<song>{
        return try {
            SongCollection.get().await().toObjects(song::class.java)
        }catch (e:Exception){
            emptyList()
        }
    }

}