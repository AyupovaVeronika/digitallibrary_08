package com.example.digitallibrary15052025.ui.main.reading

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // Добавляем отступ внизу каждого элемента, кроме последнего
        val position = parent.getChildAdapterPosition(view)
        if (position != RecyclerView.NO_POSITION && position < parent.adapter?.itemCount?.minus(1) ?: 0) {
            outRect.bottom = verticalSpaceHeight
        }
    }
}
