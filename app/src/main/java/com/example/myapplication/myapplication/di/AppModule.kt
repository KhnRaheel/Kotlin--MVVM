package com.example.myapplication.myapplication.di


import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.myapplication.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import android.content.Context
import com.example.myapplication.myapplication.adapters.SwipeSongAdapter
import com.example.myapplication.myapplication.exoplayer.MusicServiceConnection
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context
    )= MusicServiceConnection(context)
    @Singleton
    @Provides
    fun provideswipeSongadapter()= SwipeSongAdapter()
    @Singleton
    @Provides

    fun provideGlideInstance(
        @ApplicationContext context:Context)=Glide.with(context).setDefaultRequestOptions(
        RequestOptions().
                placeholder(R.drawable.ic_image).
                error(R.drawable.ic_image).
                diskCacheStrategy(DiskCacheStrategy.DATA)
        )

}