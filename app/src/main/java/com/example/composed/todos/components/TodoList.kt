package com.example.composed.todos.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.composed.todos.models.TodoItem

@Preview(showBackground = true)
@Composable
fun TodoList(
    todos: List<TodoItem> = emptyList(),
    onTodoEditingFinished: (TodoItem, String) -> Unit = { _, _ -> },
    onTodoCheckChanged: (TodoItem, Boolean) -> Unit = { _, _ -> },
) {
    LazyColumnFor(
        items = todos,
        contentPadding = PaddingValues(16.dp),
    ) { todoItem ->
        Todo(
            todoItem,
            onEditingFinished = {
                onTodoEditingFinished(
                    todoItem,
                    it,
                )
            },
            onCheckedChange = {
                onTodoCheckChanged(
                    todoItem,
                    it,
                )
            },
        )
    }
}