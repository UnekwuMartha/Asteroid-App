package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class AsteroidAdapter(private val clickListener : AsteroidClickListener) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : AsteroidViewHolder{
        val binding = AsteroidItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position : Int){
        val asteroidItem = getItem(position)
        holder.bind(asteroidItem, clickListener)
    }

    class AsteroidClickListener(val clickListener: (asteroid : Asteroid) -> Unit){
        fun onClick(asteroid : Asteroid) = clickListener(asteroid)
    }

    class DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }
    class AsteroidViewHolder(val binding : AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(asteroid: Asteroid, clickListener: Any){
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }
}