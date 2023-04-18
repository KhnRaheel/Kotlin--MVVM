package com.example.myapplication.myapplication.di

import android.content.Context
import android.media.AudioAttributes
import android.os.Build
import android.widget.SimpleExpandableListAdapter
import com.example.myapplication.myapplication.data.MusicDataBase
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.C.AUDIO_CONTENT_TYPE_MUSIC
import com.google.android.exoplayer2.C.USAGE_MEDIA
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {
    @ServiceScoped
    @Provides
    fun provdeMusicDtabase()= MusicDataBase()

    @ServiceScoped
    @Provides
    fun provideAudioattributes()=
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        com.google.android.exoplayer2.audio.AudioAttributes.Builder()
            .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(USAGE_MEDIA)
            .build()
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }

   @ServiceScoped
    @Provides
    fun provideexoplayer (
        @ApplicationContext context :Context,
        audioAttributes: com.google.android.exoplayer2.audio.AudioAttributes
    )= ExoPlayer.Builder(context).build().apply {
        setAudioAttributes(audioAttributes, true)
        setHandleAudioBecomingNoisy(true)

    }
    @ServiceScoped
    @Provides
    fun ProvideDataSourceFActory(
        @ApplicationContext context :Context
    )= DefaultDataSourceFactory(context,Util.getUserAgent(context,"myapplication"))

}