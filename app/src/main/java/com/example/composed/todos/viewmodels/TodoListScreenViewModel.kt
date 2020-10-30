package com.example.composed.todos.viewmodels

import androidx.lifecycle.ViewModel
import com.example.composed.todos.models.TodoItem
import com.example.composed.todos.models.newTodo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

abstract class TodoListScreenViewModel : ViewModel() {
    @ExperimentalCoroutinesApi
    abstract val state: MutableStateFlow<List<TodoItem>>

    abstract fun onAddTodoClicked()

    abstract fun onTodoEditFinished(todo: TodoItem, newText: String)

    abstract fun onTodoChecked(todo: TodoItem, checked: Boolean)

    abstract fun onTodoSwiped(todo: TodoItem)
}

typealias State = List<TodoItem>

@ExperimentalCoroutinesApi
class TodoListScreenViewModelImpl : TodoListScreenViewModel() {
    private object TodoItemComparator : Comparator<TodoItem> {
        override fun compare(o1: TodoItem, o2: TodoItem) =
            when {
                o1.done && !o2.done -> 1

                !o1.done && o2.done -> -1

                else -> (o2.createdAt.time - o1.createdAt.time).toInt()
            }
    }

    private val intents = Channel<Intent>()

    init {
        GlobalScope.launch {
            handleIntents()
        }
    }

    override val state: MutableStateFlow<State> = MutableStateFlow(
        listOf(
            newTodo("Buy bread").copy(editing = false),
            newTodo("Buy beef").copy(editing = false),
            newTodo("Buy coffee").copy(editing = false),
            newTodo("Buy other things").copy(editing = false),
        ).sortedWith(TodoItemComparator)
    )

    override fun onAddTodoClicked() {
        intents.offer(Intent.AddTodoIntent(newTodo("")))
    }

    override fun onTodoEditFinished(todo: TodoItem, newText: String) {
        intents.offer(
            Intent.UpdateTodoIntent(
                todo.copy(
                    title = newText,
                    editing = false,
                )
            )
        )
    }

    override fun onTodoChecked(todo: TodoItem, checked: Boolean) {
        intents.offer(Intent.UpdateTodoIntent(todo.copy(done = checked)))
    }

    override fun onTodoSwiped(todo: TodoItem) {
        intents.offer(Intent.DeleteTodoIntent(todo))
    }

    private suspend fun handleIntents() {
        intents.consumeAsFlow().collect { state.value = handleIntent(it) }
    }

    private fun handleIntent(intent: Intent): State {
        val newTodos = state.value.toMutableList()

        when (intent) {
            is Intent.AddTodoIntent ->
                newTodos.add(0, intent.todo)

            is Intent.UpdateTodoIntent -> {
                val index = newTodos.indexOfFirst { it.id == intent.todo.id }
                newTodos[index] = intent.todo
            }

            is Intent.DeleteTodoIntent ->
                newTodos.removeIf { it.id == intent.todo.id }
        }

        return newTodos.sortedWith(TodoItemComparator)
    }
}