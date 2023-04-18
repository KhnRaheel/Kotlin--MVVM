package com.example.myapplication.myapplication.ui.ViewModels

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.adapters.SwipeSongAdapter
import com.example.myapplication.myapplication.data.song
import com.example.myapplication.myapplication.exoplayer.toSong
import com.example.myapplication.myapplication.others.Status
import com.example.myapplication.myapplication.others.Status.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by  viewModels()
    @Inject
    lateinit var glide: RequestManager
    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter

    private var curPlayingSong: song?= null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

      vpSong.adapter=swipeSongAdapter


    }
    private fun funSwitchVewOAgertoCurrentSong(song: song){
        val  newItemIndex= swipeSongAdapter.song.indexOf(song)
        if(newItemIndex !=-1){
            vpSong.currentItem= newItemIndex
            curPlayingSong=song
        }

    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun SubscribetoObserver(){
        viewModel.mediItems.observe(this) {
            it?.let { result->
                when(result.status){
                    SUCESS ->{
                        result.data?.let { songs ->
                            swipeSongAdapter.song=songs
                            if(songs.isNotEmpty()){
                                glide.load(curPlayingSong ?: songs[0].imageurl).into(ivCurSongImage)
                            }
                               funSwitchVewOAgertoCurrentSong(curPlayingSong ?: return@observe)
                        }
                    }
                    ERROR->Unit
                    LOADING ->Unit
                }
            }
        }
        viewModel.curPlayingSong.observe(this){
            if(it== null)return@observe

            curPlayingSong=it.toSong()
            glide.load(curPlayingSong ).into(ivCurSongImage)

            funSwitchVewOAgertoCurrentSong(curPlayingSong ?: return@observe)


        }
    }

}


