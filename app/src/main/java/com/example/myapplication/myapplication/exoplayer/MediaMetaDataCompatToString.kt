package com.example.myapplication.myapplication.exoplayer

import android.support.v4.media.MediaMetadataCompat
import com.example.myapplication.myapplication.data.song

fun MediaMetadataCompat.toSong(): song? {
    return  description?.let {
        song(
            it.mediaId ?: " ",
            it.title.toString(),
            it.subtitle.toString(),
            it.mediaUri.toString(),
            it.iconUri.toString()

        )
    }
}