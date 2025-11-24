package com.fantasmadux.simpletodoapp

import android.graphics.Canvas
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.fantasmadux.simpletodoapp.adapter.ToDoAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class RecyclerViewTouchHelper(
    private val adapter: ToDoAdapter,

    ) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(
        viewHolder: RecyclerView.ViewHolder,
        direction: Int
    ) {
        val position = viewHolder.adapterPosition
        if (direction == ItemTouchHelper.RIGHT) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(adapter.getContext())
            builder.setTitle("Delete task")
            builder.setMessage("Are you sure u want to delete task?")
            builder.setPositiveButton("Yes") { dialog, which ->
                adapter.deleteTask(position)
            }
            builder.setNegativeButton("No") { dialog, which ->
                adapter.notifyItemChanged(position)
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        } else {
            adapter.editTask(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        ).apply {
            addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.green))
            addSwipeLeftActionIcon(R.drawable.ic_edit)

            addSwipeRightBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.red))
            addSwipeRightActionIcon(R.drawable.ic_delete)

        }.create().decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}