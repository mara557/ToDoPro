package com.example.todopro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class TodoAdapter(
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private var todoList = emptyList<Todo>()

    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val taskTextView: TextView = itemView.findViewById(R.id.taskTextView)
        val optionsImageView: ImageView = itemView.findViewById(R.id.optionsImageView)

        init {
            itemView.setOnClickListener(this)
            optionsImageView.setOnClickListener {
                listener.onOptionsClick(todoList[adapterPosition], adapterPosition, optionsImageView)
            }
        }

        override fun onClick(v: View?) {
            listener.onItemClick(todoList[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(itemView)
    }

    override fun getItemCount() = todoList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = todoList[position]
        holder.taskTextView.text = currentItem.task
    }

    fun setTodos(todos: List<Todo>) {
        this.todoList = todos
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(todo: Todo)
        fun onOptionsClick(todo: Todo, position: Int, view: View)
    }
}
