package com.example.composed.todos.components

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.composed.todos.viewmodels.TodoListScreenViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun TodoListScreen(
    viewModel: TodoListScreenViewModel
) {
    val items by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar {
                Text(text = "Todo list")
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::onAddTodoClicked) {
                Icon(asset = Icons.Filled.Add)
            }
        }) {
        TodoList(
            todos = items,
            onTodoCheckChanged = viewModel::onTodoChecked,
            onTodoEditingFinished = viewModel::onTodoEditFinished,
            onTodoSwiped = viewModel::onTodoSwiped,
        )
    }
}