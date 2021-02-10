package ru.d3st.academyandroid.actor

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.d3st.academyandroid.R

import ru.d3st.academyandroid.databinding.ActorBioFragmentBinding


class ActorBioFragment : Fragment() {

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

        val selectedActor = ActorBioFragmentArgs.fromBundle(requireArguments()).selectedActor

        vieModelFactory =
            ActorBioViewModelFactory(
                selectedActor,
                requireNotNull(activity).application
            )
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

        viewModel.navigateToMovieDetails.observe(viewLifecycleOwner, { movie ->
            movie?.let {
                this.findNavController().navigate(
                    //после подключения SafeArgs
                    ActorBioFragmentDirections.actionActorBioToMovieDetailsFragment(movie.id)
                )
                viewModel.onMovieNavigated()
            }
        })

        //наблюдение за возникновением ошибок сети
        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        return binding.root
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            navigateToPreviousScreen()
            viewModel.onNetworkErrorShown()
        }
    }


    private fun navigateToPreviousScreen() {
        this.findNavController().navigate(
            R.id.action_actorBioFragment_to_movieListFragment
        )
    }


}