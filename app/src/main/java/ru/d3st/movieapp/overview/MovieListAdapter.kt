package ru.d3st.movieapp.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.d3st.movieapp.databinding.ItemMovieListBinding
import ru.d3st.movieapp.domain.Movie

class MovieListAdapter(private val clickListener: MovieClickListener) :
    ListAdapter<Movie, MovieListAdapter.ViewHolder>(MovieDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(it, item.id)
        }
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Movie) {
            binding.movie = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMovieListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    //обработка кликов
    class MovieClickListener(val click: (view: View, movieId: Int) -> Unit) {
        fun onClick(view: View, movieId: Int) = click(view, movieId)
    }
}


//Класс для сравнения списков при обновлении
class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

}