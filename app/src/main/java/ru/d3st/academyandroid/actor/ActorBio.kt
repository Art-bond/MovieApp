package ru.d3st.academyandroid.actor

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ru.d3st.academyandroid.R
import ru.d3st.academyandroid.databinding.ActorBioFragmentBinding
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.overview.MovieListFragmentDirections


class ActorBio : Fragment() {

    private lateinit var binding: ActorBioFragmentBinding
    private lateinit var viewModel: ActorBioViewModel
    private lateinit var vieModelFactory: ActorBioViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate view and obtain an instance of the binding class
        binding = ActorBioFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        vieModelFactory =
            ActorBioViewModelFactory(ActorBioArgs.fromBundle(requireArguments()).selectedActor)
        viewModel = ViewModelProvider(this, vieModelFactory).get(ActorBioViewModel::class.java)
        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        //Привязываем адаптер
        val adapter = ActorsMovieListAdapter(ActorsMovieListener { movie ->
            viewModel.onMovieSelected(movie)
        })

        binding.actorMovieList.adapter = adapter

        viewModel.actorsMovies.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToMovieDetails.observe(viewLifecycleOwner,{movie ->
            movie?.let{
                this.findNavController().navigate(
                    //после подключения SafeArgs
                    ActorBioDirections.actionActorBioToMovieDetailsFragment(movie)
                )
                viewModel.onMovieNavigated()
            }

        })



        return binding.root
    }


}