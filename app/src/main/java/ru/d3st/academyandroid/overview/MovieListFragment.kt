package ru.d3st.academyandroid.overview

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.d3st.academyandroid.R
import ru.d3st.academyandroid.databinding.FragmentMoviesListBinding
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.notification.Notifier

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by viewModels()
    //private lateinit var viewModelFactory: MovieListViewModelFactory
    private var _bind: FragmentMoviesListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val bind get() = _bind!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        _bind = FragmentMoviesListBinding.inflate(inflater, container, false)

        //биндим ВМ
        //viewModel = ViewModelProvider(this, viewModelFactory).get(MovieListViewModel::class.java)

        //lifecycleOwner управляет жизненным циклом фрагмента
        bind.lifecycleOwner = this
        //получаем Viewmodel
        bind.movieListViewModel = viewModel


        val adapter = MovieListAdapter(MovieListAdapter.MovieClickListener { movieId ->
            viewModel.displayMovieDetailsBegin(movieId)
        })

        bind.rvMoviesList.adapter = adapter

        //bind.rvMoviesList.addItemDecoration(GridSpacingItemDecoration(10))

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
        viewModel.movies.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })



        //наблюдение за данными фильмо для загрузки в список
        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, {
            //проверяем есть ли данные фильма
            if (it != null) {
                //вызываем findNavController
                this.findNavController().navigate(
                    //после подключения SafeArgs
                    MovieListFragmentDirections.actionListToDetail(it)
                )
                //приводим пеерменную отвечающую за переход в исходное состояние
                viewModel.displaySelectedMovieComplete()
            }
        })
        //наблюдение за возникновением ошибок сети
        viewModel.eventNetworkError.observe(viewLifecycleOwner, { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })

        bind.movieListText.setOnClickListener {
            navigateToLocation()
        }

        return bind.root
    }

    private fun navigateToLocation() {
        //val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentToActorBio(actor)
        val action = MovieListFragmentDirections.actionMovieListFragmentToLocation()
        view?.findNavController()?.navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _bind = null
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

}