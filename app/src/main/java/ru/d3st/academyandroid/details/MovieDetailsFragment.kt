package ru.d3st.academyandroid.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.d3st.academyandroid.R
import ru.d3st.academyandroid.databinding.FragmentMovieDetailBinding
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val args: MovieDetailsFragmentArgs by navArgs()


    @Inject
    lateinit var viewModelFactory: MovieDetailsVIewModelFactory

    private val viewModel: MovieDetailsViewModel by viewModels {
        MovieDetailsViewModel.provideFactory(viewModelFactory, args.selectedMovie)
    }

    private lateinit var binding: FragmentMovieDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)

        binding.viewModelMovieDetail = viewModel
        //для обновления экрана
        binding.lifecycleOwner = this


        //слушатель кнопки Back
        binding.backButton.setOnClickListener {
            navigateToMovieList()
        }


        actorClick()

        //наблюдение за возникновением ошибок сети
        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })


        return binding.root
    }

    fun navigateTo(actorId: Int){
        navigateToActor(actorId)
    }


    private fun actorClick() {


/*        binding.actor1Image.setOnClickListener {
            val actorId = viewModel.actors.value?.get(0)?.id
            if (actorId != null) {
                navigateToActor(actorId)
            }
        }*/


        binding.actor2Image.setOnClickListener {
            val actor = viewModel.actors.value?.get(1)?.id
            if (actor != null) {
                navigateToActor(actor)
            }
        }
        binding.actor3Image.setOnClickListener {
            val actor = viewModel.actors.value?.get(2)?.id
            if (actor != null) {
                navigateToActor(actor)
            }
        }
        binding.actor4Image.setOnClickListener {
            val actor = viewModel.actors.value?.get(3)?.id
            if (actor != null) {
                navigateToActor(actor)
            }
        }
    }


    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            showSnackBar()
            viewModel.onNetworkErrorShown()
        }
    }

    private fun showSnackBar() {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            getString(R.string.network_error),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun navigateToMovieList() {
        val action = MovieDetailsFragmentDirections.actionMovieDetailsToMovieList()

        view?.findNavController()?.navigate(action)
    }


    private fun navigateToActor(actorId: Int) {
        val action =
            MovieDetailsFragmentDirections.actionMovieDetailsFragmentToActorBio(actorId)

        view?.findNavController()?.navigate(action)
    }


}



