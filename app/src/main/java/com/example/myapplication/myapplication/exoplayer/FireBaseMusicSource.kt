package com.example.myapplication.myapplication.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.example.myapplication.myapplication.data.MusicDataBase
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FireBaseMusicSource @Inject constructor(
    private  val musicdatabase:MusicDataBase

) {
    var songs = emptyList<MediaMetadataCompat>()
    suspend fun  fetchMediaData()= withContext(Dispatchers.Main){
        state=State.STATE_INITIALIZING
        val allSongs=musicdatabase.getAllSongs()
        songs= allSongs.map { song->
            MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_ARTIST, song.subtitle)
                .putString(METADATA_KEY_MEDIA_ID, song.mediaid)
                .putString(METADATA_KEY_TITLE, song.title)
                .putString(METADATA_KEY_DISPLAY_TITLE,song.title)
                .putString(METADATA_KEY_DISPLAY_ICON_URI,song.imageurl)
                .putString(METADATA_KEY_MEDIA_URI, song.songurl)
                .putString(METADATA_KEY_ALBUM_ART_URI, song.imageurl)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, song.subtitle)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, song.subtitle)
                .build()
        }
        state= State.STATET_INITIALIZED

    }
    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory):ConcatenatingMediaSource{
        val concatenatingMediaSource=ConcatenatingMediaSource()
        songs.forEach{song->
            val mediaSource= ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI).toUri()))
            concatenatingMediaSource.addMediaSource(mediaSource)

        }
        return  concatenatingMediaSource
    }
    fun asMediaItem()= songs.map { song->
        val desc= MediaDescriptionCompat.Builder()
            .setMediaUri(song.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(song.description.title)
            .setSubtitle(song.description.subtitle)
            .setMediaId(song.description.mediaId)
            .setIconUri(song.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()
    private val onReadyListner= mutableListOf<(Boolean)-> Unit>()
    @OptIn(InternalCoroutinesApi::class)
    private var state:State=State.STATE_CREATED
    set(value) {
        if(value== State.STATET_INITIALIZED || value== State.STATE_ERROR){
            synchronized(onReadyListner){
                field=value
                onReadyListner.forEach { listner->
                    listner(state== State.STATET_INITIALIZED) //lambda function take state as perimeter and return true
                } }

            }else{
                field= value

        }
    }
    fun whenReady(action:(Boolean)->Unit):Boolean{
        if(state==State.STATE_CREATED || state ==State.STATE_INITIALIZING){
              onReadyListner +=action
              return false

        }else
        {
            action(state==State.STATET_INITIALIZED)
            return  true
        }
    }


}
enum class  State{
    STATE_CREATED,
    STATE_INITIALIZING,
    STATET_INITIALIZED,
    STATE_ERROR

}