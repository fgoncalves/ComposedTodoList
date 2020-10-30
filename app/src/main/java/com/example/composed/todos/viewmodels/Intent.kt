package com.example.composed.todos.viewmodels

import com.example.composed.todos.models.TodoItem

sealed class Intent {
    data class AddTodoIntent(
        val todo: TodoItem,
    ) : Intent()

    data class UpdateTodoIntent(
        val todo: TodoItem,
    ) : Intent()

    data class DeleteTodoIntent(
        val todo: TodoItem,
    ) : Intent()
}