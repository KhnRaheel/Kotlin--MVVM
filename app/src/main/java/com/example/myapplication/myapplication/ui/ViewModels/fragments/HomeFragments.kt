package com.example.myapplication.myapplication.ui.ViewModels.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.adapters.SongAdapter
import com.example.myapplication.myapplication.others.Status
import com.example.myapplication.myapplication.ui.ViewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
//extend from fragment and pass
class HomeFragments :Fragment(R.layout.fragment_home) {

    lateinit var  mainViewModel: MainViewModel
    @Inject
    lateinit var  songAdapter: SongAdapter

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel= ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setUpRecyclerView()
        subscribeToObserver()
        songAdapter.setOnItemClickListner {
            mainViewModel.playOrToggleSong(it,)
        }
    }
    private  fun setUpRecyclerView()= rvAllSongs.apply {
        adapter=songAdapter
        layoutManager= LinearLayoutManager(requireContext())
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)

    private fun subscribeToObserver(){
        mainViewModel.mediItems.observe(viewLifecycleOwner){ result->
            when(result.status){
                Status.SUCESS->{
                    allSongsProgressBar.isVisible=false
                }
                Status.ERROR-> Unit
                Status.LOADING->{
                    allSongsProgressBar.isVisible=true
                    result.data?.let{song ->
                        songAdapter.song= song

                    }
                }
            }
        }
    }

}