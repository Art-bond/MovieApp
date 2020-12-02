package ru.d3st.academyandroid.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.d3st.academyandroid.databinding.FragmentMovieDetailBinding


class MovieDetailsFragment : Fragment() {

    private lateinit var viewModel: MovieDetailsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        //получаем данные из предыдущего фрагмента

        val movieOne = MovieDetailsFragmentArgs.fromBundle(requireArguments()).selectedMovie
        //создаем экземпляр ViewModelFactory, для того чтобы поместить данные из предыдущего фрагмента в ВьюМодел этого фрагмента
       val viewModelFactory = MovieDetailsVIewModelFactory(movieOne)
        //биндим ВМ
        viewModel = ViewModelProvider(this,viewModelFactory).get(MovieDetailsViewModel::class.java)
        binding.viewModelMovieDetail = viewModel
        //для обновления экрана
        binding.lifecycleOwner = this
        return binding.root
    }
}