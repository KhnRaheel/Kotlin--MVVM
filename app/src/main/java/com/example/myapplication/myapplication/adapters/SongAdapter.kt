package com.example.myapplication.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.song
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private  val glide: RequestManager
):BaseSongAdapter(R.layout.list_item) {

    override val differ= AsyncListDiffer(this,diffCallback)


    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
       val song= song[position]
        holder.itemView.apply {
            tvPrimary.text=song.title
            tvSecondary.text=song.subtitle
            glide.load(song.songurl).into(ivItemImage)
            setOnClickListener{
                onItemClickListener?.let {click ->
                    click(song)
                }
            }

        }
    }

}