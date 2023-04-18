package com.example.myapplication.myapplication.exoplayer.callBcks

import android.widget.Toast
import com.example.myapplication.myapplication.exoplayer.MusicService
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player

class MusicPlayerEventListner(
    private val MusicService: MusicService

):Player.Listener{
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if(playbackState==Player.STATE_READY&& !playWhenReady){
            MusicService.stopForeground(false)

        }


    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(MusicService,"errroe occured ",Toast.LENGTH_LONG).show()
    }
}