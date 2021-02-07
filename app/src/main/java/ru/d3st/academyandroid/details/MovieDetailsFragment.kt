package ru.d3st.academyandroid.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ru.d3st.academyandroid.actor.ActorBioViewModel
import ru.d3st.academyandroid.actor.ActorBioViewModelFactory
import ru.d3st.academyandroid.database.MovieDao
import ru.d3st.academyandroid.database.MoviesDataBase
import ru.d3st.academyandroid.databinding.ActorBioFragmentBinding
import ru.d3st.academyandroid.databinding.FragmentMovieDetailBinding
import ru.d3st.academyandroid.domain.Actor
import ru.d3st.academyandroid.repository.MoviesRepository
import java.util.Calendar.getInstance


class MovieDetailsFragment : Fragment() {

    private lateinit var viewModel: MovieDetailsViewModel
    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var viewModelFactory: MovieDetailsVIewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {

        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        //через фрагмент получаем доступ к приложению
        val application = requireNotNull(activity).application

        //получаем данные из предыдущего фрагмента
        val movieId = MovieDetailsFragmentArgs.fromBundle(requireArguments()).selectedMovie


        //создаем экземпляр ViewModelFactory, для того чтобы поместить данные из предыдущего фрагмента в ВьюМодел этого фрагмента

       viewModelFactory = MovieDetailsVIewModelFactory(application, movieId)
        //биндим ВМ
        viewModel = ViewModelProvider(this,viewModelFactory).get(MovieDetailsViewModel::class.java)
        binding.viewModelMovieDetail = viewModel
        //для обновления экрана
        binding.lifecycleOwner = this



        //находим кнопку Back и вешаем на нее функцию перехода на экран Списка Фильмов
        binding.backButton.setOnClickListener {
            navigateToMovieList()
        }
            //TODO шаблонный код, надо чтонибудь придумать

        binding.actor1Image.setOnClickListener {
            val actor = viewModel.actors.value?.get(0)
            if (actor != null) {
                navigateToActor(actor)
            }
        }
        binding.actor2Image.setOnClickListener {
            val actor = viewModel.actors.value?.get(1)
            if (actor != null) {
                navigateToActor(actor)
            }
        }
        binding.actor3Image.setOnClickListener {
            val actor = viewModel.actors.value?.get(2)
            if (actor != null) {
                navigateToActor(actor)
            }
        }
        binding.actor4Image.setOnClickListener {
            val actor = viewModel.actors.value?.get(3)
            if (actor != null) {
                navigateToActor(actor)
            }
        }


        return binding.root
    }

    private fun navigateToMovieList() {
        val action = MovieDetailsFragmentDirections.actionMovieDetailsToMovieList()

        view?.findNavController()?.navigate(action)
    }



    private fun navigateToActor(actor: Actor) {
        val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentToActorBio(actor)

        view?.findNavController()?.navigate(action)
    }
}