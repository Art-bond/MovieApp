package ru.d3st.movieapp.overview

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import ru.d3st.movieapp.R
import ru.d3st.movieapp.databinding.FragmentMoviesListBinding
import ru.d3st.movieapp.location.LocationFragment
import ru.d3st.movieapp.network.Resource
import timber.log.Timber

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by viewModels()

    private lateinit var binding: FragmentMoviesListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        //lifecycleOwner управляет жизненным циклом фрагмента
        binding.lifecycleOwner = this
        //получаем Viewmodel
        binding.viewModel = viewModel


        val adapter = MovieListAdapter(MovieListAdapter.MovieClickListener { view, movieId ->
            navigateToMovie(view, movieId)
        })

        binding.rvMoviesList.adapter = adapter

        //Так как Превью фильма в списке имеет фиксированное значение,
        //получаем его ширину в пикселях исходя из DP данного устройства
        val itemWidth = resources.getDimensionPixelSize(R.dimen.recycler_view_item_width)

        val orientation: Int = resources.configuration.orientation
        //в зависимости от ориентации будет выбираться количество столбцов либо 2 либо 4
        val spanCount = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //in landscape
            4
        } else {
            // In portrait
            2
        }

        val manager = GridLayoutManager(
            activity,
            spanCount
        )
        binding.rvMoviesList.layoutManager = manager
        //настраиваем равные отступы и центрирование элементов в нашем списке(сетке)
        binding.rvMoviesList.addItemDecoration(GridSpacingItemDecoration(spanCount, itemWidth))

        //observing movie list
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is Resource.Success ->
                        if (!state.data.isNullOrEmpty()) {
                            binding.movieState.movieListState.visibility = View.GONE
                            adapter.submitList(state.data)
                        }
                    else -> Timber.e("ui state error")
                }
            }
        }
        //observe on network query state
        lifecycleScope.launchWhenCreated {
            viewModel.networkState.collect{
                    networkState ->
                when(networkState){
                    is Resource.Failure -> {
                        if (adapter.itemCount == 0) {
                            networkState.message?.let { onFailureState(it) }
                        }
                        showSnackBar(networkState.message)
                    }
                    is Resource.InProgress -> {
                        if (adapter.itemCount == 0) {
                            onLoadingState()
                        }
                        showSnackBar("Loading Data")
                    }
                    is Resource.Success -> showSnackBar("data base has been refreshed")
                }
            }
        }

        //handle click on Retry Button
        binding.retryCallback = object : OverviewRetryCallBack {
            override fun retry() {
                viewModel.retry()
            }
        }

        /**navigate to [LocationFragment]**/
        binding.movieListText.setOnClickListener {
            navigateToLocation()
        }
        return binding.root
    }

    private fun onLoadingState() {
        binding.movieState.movieListState.visibility = View.VISIBLE
        binding.movieState.progressBar.visibility = View.VISIBLE
        binding.movieState.errorMsg.visibility = View.GONE
        binding.movieState.retry.visibility = View.GONE
    }

    private fun onFailureState(
        errorMessage: String
    ) {
        binding.movieState.movieListState.visibility = View.VISIBLE
        binding.movieState.progressBar.visibility = View.GONE
        binding.movieState.errorMsg.text = errorMessage
        binding.movieState.errorMsg.visibility = View.VISIBLE
        binding.movieState.retry.visibility = View.VISIBLE
    }


    private fun navigateToMovie(view: View, movieId: Int) {
/*        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }*/
        /*        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }*/
        Hold().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        exitTransition = Hold().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }

        reenterTransition = Hold().apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        val movieCardTransactionName = getString(R.string.movie_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(view to movieCardTransactionName)
        val directions = MovieListFragmentDirections.actionListToDetail(movieId)
        findNavController().navigate(directions, extras)
    }

    private fun navigateToLocation() {
        val action = MovieListFragmentDirections.actionMovieListFragmentToLocation()
        view?.findNavController()?.navigate(action)
    }

    private fun showSnackBar(exception: String?) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            exception.toString(),
            Snackbar.LENGTH_LONG
        ).show()
    }

}

interface OverviewRetryCallBack {
    fun retry()
}