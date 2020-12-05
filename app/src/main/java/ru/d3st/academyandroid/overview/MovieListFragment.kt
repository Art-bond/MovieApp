package ru.d3st.academyandroid.overview

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.d3st.academyandroid.R
import ru.d3st.academyandroid.databinding.FragmentMoviesListBinding


class MovieListFragment : Fragment() {

    private val viewModel: MovieListViewModel by lazy {
        ViewModelProvider(this).get(MovieListViewModel::class.java)
    }

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


        //lifecycleOwner управляет жизненным циклом фрагмента
        bind.lifecycleOwner = this
        //получаем Viewmodel
        bind.movieListViewModel = viewModel


        val adapter = MovieListAdapter(MovieListAdapter.ClickListener { movie ->
            viewModel.displayMovieDetails(movie)
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

        viewModel.movieList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })


        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner, {
            //проверяем есть ли данные фильма
            if (it != null) {
                //вызываем финдНавКонтроллер
                this.findNavController().navigate(
                    //после подключения SafeArgs
                    MovieListFragmentDirections.actionListToDetail(it)
                )
                //приводим пеерменную отвечающую за переход в исходное состояние
                viewModel.displaySelectedGroupComplete()
            }
        })

        return bind.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _bind = null
    }

}