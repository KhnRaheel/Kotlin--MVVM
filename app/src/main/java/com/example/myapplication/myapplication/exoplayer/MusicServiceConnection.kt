package com.example.myapplication.myapplication.exoplayer

import android.content.ComponentName
import android.content.Context
import android.media.browse.MediaBrowser
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.MediaController
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.myapplication.data.Constants
import com.example.myapplication.myapplication.others.Event
import com.example.myapplication.myapplication.others.Resource

class MusicServiceConnection(
    context: Context
) {
    private  val _isConnected= MutableLiveData<Event<Resource<Boolean>>>()
    val isConnected:LiveData<Event<Resource<Boolean>>> = _isConnected
    private  val _networkError= MutableLiveData<Event<Resource<Boolean>>>()
    val networkError:LiveData<Event<Resource<Boolean>>> = _networkError

    private  val _playBackState= MutableLiveData<PlaybackStateCompat?>()
    val playBackState:LiveData<PlaybackStateCompat?> = _playBackState

    private  val _curPlayingSong= MutableLiveData<MediaMetadataCompat?>()
    val curPlayingSong:LiveData<MediaMetadataCompat?> = _curPlayingSong

    lateinit var  mediaController: MediaControllerCompat
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val mediaBrowserConnectionCallBack= MediaBrowserConnectionCallBack(context)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private  val mediaBrowser= MediaBrowserCompat(context,ComponentName(context,MusicService::class.java
        ),
        mediaBrowserConnectionCallBack,null
    ).apply { connect() }

    val transportControls:MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun suscribe(parenId:String, callback:MediaBrowserCompat.SubscriptionCallback){
        mediaBrowser.subscribe(parenId,callback)

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun unsuscribe(parenId:String, callback:MediaBrowserCompat.SubscriptionCallback){
        mediaBrowser.unsubscribe(parenId,callback)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private inner class  MediaBrowserConnectionCallBack(
        private val context: Context
    ): MediaBrowserCompat.ConnectionCallback(){
        override fun onConnected() {
           mediaController=MediaControllerCompat(context,mediaBrowser.sessionToken).apply {
               registerCallback(MediaControllerCallback())
           }
            _isConnected.postValue(Event(Resource.sucess(true)))

        }

        override fun onConnectionSuspended() {
            _isConnected.postValue(Event(Resource.error("The connection was suspended", false)))
        }

        override fun onConnectionFailed() {
            _isConnected.postValue(Event(Resource.error("Couldnt connect to browser ", false)))
        }
    }
    private  inner class MediaControllerCallback:MediaControllerCompat.Callback(){
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {

            _playBackState.postValue(state)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            _curPlayingSong.postValue(metadata)



        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            when(event){
                Constants.NETWORK_ERROR-> _networkError.postValue(
                    Event(
                    Resource.error("Cannot connect to server Please check your net connection",null)
                ))
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallBack.onConnectionSuspended()
        }

    }
}