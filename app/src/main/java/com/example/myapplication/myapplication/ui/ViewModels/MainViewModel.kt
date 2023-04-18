package com.example.myapplication.myapplication.ui.ViewModels

import android.os.Build
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.SubscriptionCallback
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.myapplication.data.Constants.MEDIA_ROOT_ID
import com.example.myapplication.myapplication.data.song
import com.example.myapplication.myapplication.exoplayer.MusicServiceConnection
import com.example.myapplication.myapplication.exoplayer.isPLAyEnabled
import com.example.myapplication.myapplication.exoplayer.isPlaying
import com.example.myapplication.myapplication.exoplayer.isPrepared
import com.example.myapplication.myapplication.others.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection
): ViewModel() {
    private val _mediaItems= MutableLiveData<Resource<List<song>>>()
    val mediItems: LiveData<Resource<List<song>>> = _mediaItems

    val isConnected= musicServiceConnection.isConnected
    val networkerror= musicServiceConnection.networkError

    val curPlayingSong= musicServiceConnection.curPlayingSong


    val playBackState= musicServiceConnection.playBackState

    init {
        _mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.suscribe(MEDIA_ROOT_ID, object:MediaBrowserCompat.SubscriptionCallback(){
            override fun onChildrenLoaded(
                parentId: String,
                children: MutableList<MediaBrowserCompat.MediaItem>
            ) {
                super.onChildrenLoaded(parentId, children)
                val items= children.map {
                    song(
                        it.mediaId!!,
                        it.description.title.toString(),
                        it.description.subtitle.toString(),
                        it.description.mediaUri.toString(),
                        it.description.iconUri.toString()

                    )

                }
                _mediaItems.postValue(Resource.sucess(items))
            }
        })
    }


    fun  skipToNextSong(){
        musicServiceConnection.transportControls.skipToNext()

    }
    fun  skipToPreviousSong(){
        musicServiceConnection.transportControls.skipToPrevious()

    }
    fun  seekTo(pos:Long){
        musicServiceConnection.transportControls.seekTo(pos)

    }
    fun playOrToggleSong(mediaItem:song, toggle:Boolean=false){
        val isPrepared= playBackState.value?.isPrepared?:false
        if(isPrepared && mediaItem.mediaid== curPlayingSong?.value?.
            getString(METADATA_KEY_MEDIA_ID)){
            playBackState.value?.let {playBackState->
                when{
                    playBackState.isPlaying-> if (toggle) musicServiceConnection.transportControls.pause()
                    playBackState.isPLAyEnabled-> musicServiceConnection.transportControls.play()
                    else -> Unit

                }

            }
        }else{
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaid,null)
        }

    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsuscribe(MEDIA_ROOT_ID, object:SubscriptionCallback(){

        })
    }
}

