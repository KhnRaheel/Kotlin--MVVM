package com.example.myapplication.myapplication.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.IMediaControllerCallback
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.Constants.CHANNEL_ID
import com.example.myapplication.myapplication.data.Constants.NOTIFICATION_ID
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager


class MusicNotificationMAnager(
   private val  context: Context,
    sessionToken:MediaSessionCompat.Token,
    notificationlistner:PlayerNotificationManager.NotificationListener,
   private  val newSongCallback: ()->Unit


) {
    private val notificationManager:PlayerNotificationManager
    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        notificationManager = PlayerNotificationManager.Builder(
            context,
            NOTIFICATION_ID, CHANNEL_ID)
            .setChannelNameResourceId(R.string.notification_Channel_name)
            .setChannelDescriptionResourceId(R.string.notification_Channel_Description)
            .setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            .setNotificationListener(notificationlistner)
            .setSmallIconResourceId(R.drawable.ic_music)
            .build()

    }
    fun showNotiification(player: Player){
        notificationManager.setPlayer(player)
    }
    private inner class DescriptionAdapter(
        private val mediaController:MediaControllerCompat
    ):PlayerNotificationManager.MediaDescriptionAdapter{
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return mediaController.metadata.description.title.toString()
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {

            return mediaController.sessionActivity
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
             return  mediaController.metadata.description.subtitle.toString()
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
           Glide.with(context).asBitmap()
               .load(mediaController.metadata.description.iconUri)
               .into(object :CustomTarget<Bitmap>(){
                   override fun onResourceReady(
                       resource: Bitmap,
                       transition: Transition<in Bitmap>?
                   ) {
                       callback.onBitmap(resource)
                   }

                   override fun onLoadCleared(placeholder: Drawable?) =Unit

               })
            return null
        }
    }

}