package com.example.composed.todos.models

import java.util.*

data class TodoItem(
    val id: String,
    val title: String,
    val done: Boolean,
    val editing: Boolean,
    val createdAt: Date,
)

fun newTodo(title: String) =
    TodoItem(
        id = UUID.randomUUID().toString(),
        title = title,
        done = false,
        editing = true,
        createdAt = Date()
    )