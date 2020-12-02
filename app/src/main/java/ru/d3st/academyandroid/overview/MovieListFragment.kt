package ru.d3st.academyandroid.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        _bind = FragmentMoviesListBinding.inflate(inflater,container, false)


        //lifecycleOwner управляет жизненным циклом фрагмента
        bind.lifecycleOwner = this
        //получаем Viewmodel
        bind.movieListViewModel = viewModel

        val adapter = MovieListAdapter(MovieListAdapter.ClickListener{ movie ->
            viewModel.displayMovieDetails(movie)
        })
        bind.rvMoviesList.adapter = adapter

        viewModel.movieList.observe(viewLifecycleOwner,{
            it?.let{
                adapter.submitList(it)
            }
        })

        viewModel.navigateToSelectedMovie.observe(viewLifecycleOwner,{
            //проверяем есть ли данные фильма
            if (it != null){
                //вызываем финдНавКонтроллер
                this.findNavController().navigate(
                    //после подключения SafeArgs
                MovieListFragmentDirections.actionListToDetail(it))
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