package com.example.myapplication.myapplication.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaDrm.MediaDrmStateException
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat

import androidx.annotation.RequiresApi
import androidx.media.MediaBrowserServiceCompat
import com.example.myapplication.myapplication.data.Constants
import com.example.myapplication.myapplication.data.Constants.NETWORK_ERROR
import com.example.myapplication.myapplication.di.ServiceModule_ProvideDataSourceFActoryFactory
import com.example.myapplication.myapplication.exoplayer.callBcks.MusicPlaybackPrepare
import com.example.myapplication.myapplication.exoplayer.callBcks.MusicPlayerEventListner
import com.example.myapplication.myapplication.exoplayer.callBcks.MusicPlayerNotificationLIstner
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dagger.Provides
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject

private const val  SERVICE_TAG="MusicService"

@AndroidEntryPoint
class MusicService: MediaBrowserServiceCompat() {
    @Inject
    lateinit var fireBaseMusicSource: FireBaseMusicSource
    @Inject
    lateinit var dataSourcefactory:DefaultDataSourceFactory
    @Inject
    lateinit var  exoplayer:ExoPlayer
    private lateinit var  musicNotificationMAnager: MusicNotificationMAnager
    private lateinit var  musicPlayerEventListner:MusicPlayerEventListner

    private val serviceJob= Job()
    private  val serviceScope= CoroutineScope(Dispatchers.Main+ serviceJob)
    private  lateinit var mediasession:MediaSessionCompat
    private  lateinit var mediaSessionConnector: MediaSessionConnector
    var isForgroundService=false
    private var curPlayingSong:MediaMetadataCompat?=null
    private var isPlayerInitialized=false
    companion object{
        var curSongDuration=0L
            private  set

    }
    override fun onCreate() {
        super.onCreate()
        serviceScope.launch {
            fireBaseMusicSource.fetchMediaData()
        }


        val activityIntent = packageManager?.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, 0)
        }
        mediasession = MediaSessionCompat(this, SERVICE_TAG).apply {
            setSessionActivity(activityIntent)
            isActive = true
        }
        sessionToken = mediasession.sessionToken
        musicNotificationMAnager= MusicNotificationMAnager(
            this,mediasession.sessionToken,MusicPlayerNotificationLIstner(this)
        ){
            curSongDuration=exoplayer.duration

        }
        val mussicPlaybackPrepare= MusicPlaybackPrepare(fireBaseMusicSource){
             curPlayingSong=it
            preparePlayer(fireBaseMusicSource.songs,it,true)

        }
        mediaSessionConnector = MediaSessionConnector(mediasession)
        mediaSessionConnector.setPlaybackPreparer(mussicPlaybackPrepare)
        mediaSessionConnector.setQueueNavigator(MusicQueeeNavigator())
        mediaSessionConnector.setPlayer(exoplayer)
        musicPlayerEventListner = MusicPlayerEventListner(this)
        exoplayer.addListener(musicPlayerEventListner)
        musicNotificationMAnager.showNotiification(exoplayer)

    }
    private inner class MusicQueeeNavigator:TimelineQueueNavigator(mediasession){
        override fun getMediaDescription(
            player: Player,
            windowIndex: Int
        ): MediaDescriptionCompat {
            return  fireBaseMusicSource.songs[windowIndex].description
        }

    }
    private  fun preparePlayer(
        songs:List<MediaMetadataCompat>,
        itemtoplay:MediaMetadataCompat?,
        playNow:Boolean
    ){
        val cursSongIndex= if(curPlayingSong==null)0 else songs.indexOf(itemtoplay)
        exoplayer.setMediaSource(fireBaseMusicSource.asMediaSource(dataSourcefactory))
        exoplayer.seekTo(cursSongIndex,0)
        exoplayer.playWhenReady=playNow


    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoplayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        exoplayer.release()
        exoplayer.removeListener(musicPlayerEventListner)

    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {

        return  BrowserRoot(Constants.MEDIA_ROOT_ID,null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when(parentId){
            Constants.MEDIA_ROOT_ID->{
                val resultsent= fireBaseMusicSource.whenReady { isInitialized->
                    if(isInitialized){
                        result.sendResult(fireBaseMusicSource.asMediaItem())
                        if(!isPlayerInitialized && fireBaseMusicSource.songs.isNotEmpty() ){
                            preparePlayer(fireBaseMusicSource.songs,fireBaseMusicSource.songs[0],false)
                            isPlayerInitialized=true
                        }
                    }else
                    {   mediasession.sendSessionEvent(NETWORK_ERROR,null)
                        result.sendResult(null)
                    }

                }
                if(!resultsent){
                    result.detach()
                }
            }
        }

    }

}