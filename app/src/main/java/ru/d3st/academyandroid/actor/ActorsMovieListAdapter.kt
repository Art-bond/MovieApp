package ru.d3st.academyandroid.actor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.d3st.academyandroid.databinding.ItemActorMovieListBinding
import ru.d3st.academyandroid.domain.Movie

class ActorsMovieListAdapter(private val clickListener: ActorsMovieListener) :
    ListAdapter<Movie, ActorsMovieListAdapter.ActorsMovieViewHolder>(
        ActorsMovieDiffCallBack()
    ) {

    class ActorsMovieViewHolder(private val binding: ItemActorMovieListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cast: Movie) {
            binding.movie = cast
            binding.executePendingBindings()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsMovieViewHolder {
        //Создаем инфлейтер
        val layoutInflater = LayoutInflater.from(parent.context)
        //Создаем привязку к макету
        val binding = ItemActorMovieListBinding.inflate(layoutInflater, parent, false)
        return ActorsMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorsMovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.itemView.setOnClickListener {
            clickListener.onClick(movie)
        }
        if (movie != null) {
            holder.bind(movie)
        }
    }
}

class ActorsMovieListener(val clickListener: (movie: Movie) -> Unit) {
    fun onClick(movie: Movie) = clickListener(movie)
}


class ActorsMovieDiffCallBack : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }

}