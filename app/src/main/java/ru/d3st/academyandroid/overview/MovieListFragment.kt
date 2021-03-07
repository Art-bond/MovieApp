package ru.d3st.academyandroid.overview

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import ru.d3st.academyandroid.LocationFragment
import ru.d3st.academyandroid.R
import ru.d3st.academyandroid.databinding.FragmentMoviesListBinding
import ru.d3st.academyandroid.network.RetryCallback

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by viewModels()

    private var _bind: FragmentMoviesListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bind get() = _bind!!


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
        savedInstanceState: Bundle?,
    ): View {


        // Inflate the layout for this fragment
        _bind = FragmentMoviesListBinding.inflate(inflater, container, false)

        //lifecycleOwner управляет жизненным циклом фрагмента
        bind.lifecycleOwner = this
        //получаем Viewmodel
        bind.movieListViewModel = viewModel

        bind.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.fetchMoviesData()
            }

        }


        val adapter = MovieListAdapter(MovieListAdapter.MovieClickListener { view, movieId ->
            navigateToMovie(view, movieId)
        })

        bind.rvMoviesList.adapter = adapter

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
        bind.rvMoviesList.layoutManager = manager
        //настраиваем равные отступы и центрирование элементов в нашем списке(сетке)
        bind.rvMoviesList.addItemDecoration(GridSpacingItemDecoration(spanCount, itemWidth))

        //tracking for list
        viewModel.moviesList.observe(viewLifecycleOwner, {
            it.let {
                adapter.submitList(it.data)

            }
        })

        /**navigate to [LocationFragment]**/
        bind.movieListText.setOnClickListener {
            navigateToLocation()
        }

        return bind.root
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

    override fun onDestroy() {
        super.onDestroy()
        _bind = null
    }
}
