package com.example.myapplication.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.myapplication.R
import com.example.myapplication.myapplication.data.song
import kotlinx.android.synthetic.main.list_item.view.*

abstract class BaseSongAdapter(
    private val layouyID:Int
):RecyclerView.Adapter<BaseSongAdapter.SongViewHolder>()
{

    class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    protected val diffCallback = object: DiffUtil.ItemCallback<song>(){
        override fun areItemsTheSame(oldItem: song, newItem: song): Boolean {
            return oldItem.mediaid == newItem.mediaid

        }

        override fun areContentsTheSame(oldItem: song, newItem: song): Boolean {
            return  oldItem.hashCode()== newItem.hashCode()

        }
    }
    protected abstract val differ: AsyncListDiffer<song>

    var  song: List<song>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            LayoutInflater.from(parent.context).inflate(layouyID,
            parent,
            false))
    }


    protected var onItemClickListener:((song)-> Unit)?= null
    fun setOnItemClickListner(listner:(song)->Unit){
        onItemClickListener= listner
    }

    override fun getItemCount(): Int {
        return song.size
    }
}