package com.afrakhteh.musicplayer.views.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.afrakhteh.musicplayer.databinding.MusicItemRowBinding
import com.afrakhteh.musicplayer.model.entity.MusicEntity

class AllMusicAdapter(
        private val click: (MusicEntity) ->Unit
) : ListAdapter<MusicEntity, AllMusicAdapter.MusicViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val inflate = LayoutInflater.from(parent.context)
        val binding = MusicItemRowBinding.inflate(inflate, parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }

    class MusicViewHolder(private val binding: MusicItemRowBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MusicEntity, click: (MusicEntity) -> Unit) {
            with(binding) {
                musicItemRowLinear.setOnClickListener { click.invoke(data) }
                musicItemRowNameTv.text = data.name
                musicItemRowSingerTv.text = data.artist
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<MusicEntity>() {
        override fun areItemsTheSame(oldItem: MusicEntity, newItem: MusicEntity): Boolean {
            return oldItem?.name == newItem?.name
        }
        override fun areContentsTheSame(oldItem: MusicEntity, newItem: MusicEntity): Boolean {
            return oldItem == newItem
        }

    }


}