package ru.d3st.movieapp.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import ru.d3st.movieapp.R
import ru.d3st.movieapp.databinding.FragmentMovieDetailBinding
import ru.d3st.movieapp.utils.themeColor
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

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
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        actorClick()

        return binding.root
    }

    private fun actorClick() {

            binding.actor1Image.setOnClickListener {
                val actorId = viewModel.actors.value?.get(0)?.id
                if (actorId != null) {
                    navigateToActor(actorId)
                }
            }

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

    private fun navigateToActor(actorId: Int) {
        val action =
            MovieDetailsFragmentDirections.actionMovieDetailsFragmentToActorBio(actorId)

        view?.findNavController()?.navigate(action)
    }
}



