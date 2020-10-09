package com.example.composed.todos.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.example.composed.todos.models.TodoItem
import com.example.composed.todos.models.newTodo

@Preview(showBackground = true)
@Composable
fun Todo(
    todo: TodoItem = newTodo("Buy bread"),
    onCheckedChange: (Boolean) -> Unit = {},
    onEditingFinished: (String) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp),
    ) {
        Checkbox(checked = todo.done, onCheckedChange = onCheckedChange)

        Spacer(modifier = Modifier.preferredWidth(8.dp))

        if (todo.editing)
            EditTodo(todo, onEditingFinished = onEditingFinished)
        else
            TodoText(todo = todo)
    }
}

@Composable
private fun TodoText(todo: TodoItem) {
    Text(
        text = todo.title,
        style = if (todo.done)
            TextStyle(
                textDecoration = TextDecoration.LineThrough,
                color = Color.LightGray,
                fontSize = 18.sp,
            )
        else
            TextStyle(
                textDecoration = TextDecoration.None,
                color = Color.Black,
                fontSize = 18.sp,
            )
    )
}

@Composable
private fun EditTodo(todo: TodoItem, onEditingFinished: (String) -> Unit) {
    var textFieldValue by remember { mutableStateOf(TextFieldValue(todo.title)) }

    OutlinedTextField(
        value = textFieldValue,
        textStyle = TextStyle(
            fontSize = 18.sp,
        ),
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Text,
        onImeActionPerformed = { action, controller ->
            if (action == ImeAction.Done) {
                onEditingFinished(textFieldValue.text)
                controller?.hideSoftwareKeyboard()
            }
        },
        onValueChange = {
            textFieldValue = it
        },
    )
}
