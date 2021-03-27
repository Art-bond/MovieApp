package ru.d3st.movieapp.actor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ru.d3st.movieapp.databinding.ActorBioFragmentBinding
import javax.inject.Inject

@AndroidEntryPoint
class ActorBioFragment : Fragment() {

    private val args: ActorBioFragmentArgs by navArgs()

    @Inject
    lateinit var vieModelFactory: ActorBioViewModelFactory

    private val viewModel: ActorBioViewModel by viewModels {
        ActorBioViewModel.provideFactory(vieModelFactory, args.selectedActorId)
    }

    private lateinit var binding: ActorBioFragmentBinding

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

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }

        //Привязываем адаптер
        val adapter = ActorsMovieListAdapter(ActorsMovieListener { movie ->
            viewModel.onMovieSelected(movie)
        })
        binding.actorMovieList.adapter = adapter

        viewModel.actorsMovies.observe(viewLifecycleOwner, { movies ->
            if (movies != null) {
                adapter.submitList(movies)
            }
        })

        viewModel.navigateToMovieDetails.observe(viewLifecycleOwner, { movie ->
            movie?.let {
                this.findNavController().navigate(
                    //после подключения SafeArgs
                    ActorBioFragmentDirections.actionActorBioToMovieDetailsFragment(movie.id)
                )
                viewModel.onMovieNavigated()
            }
        })
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }


        return binding.root
    }
}

interface RetryCallback {
    fun retry()
}