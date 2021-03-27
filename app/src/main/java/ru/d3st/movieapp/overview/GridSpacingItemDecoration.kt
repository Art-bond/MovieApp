package ru.d3st.movieapp.overview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber


class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val itemWidth: Int
) : RecyclerView.ItemDecoration() {



    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        //получаем позицию элемента в нашем списке
        val position = parent.getChildAdapterPosition(view)

        //определяем положение элемента в СТРОКЕ сетки
        val column = position % spanCount
        //получаем ширину самогу RecyclerView
        val widthRecyclerView = parent.width
        Timber.i("Ширина $parent - $widthRecyclerView")

        //Вычисляем расстояние между элементами
        val spacing: Int = (widthRecyclerView - itemWidth * spanCount) / (spanCount + 1)

        //задаем отсупы слева от элементов
        outRect.left = spacing - column * spacing / spanCount
        //задаем отсупы слева от элементов
        outRect.right = (column + 1) * spacing / spanCount

        if (position < spanCount) {
            outRect.top = spacing
        }
        outRect.bottom = spacing

        Timber.i("position $position column $column left ${outRect.left} right ${outRect.right}")

        //outRect.bottom = spacing; // item bottom

    }
}


