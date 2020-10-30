package com.example.composed.todos.components

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
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
    todo: TodoItem = newTodo("Buy bread").copy(editing = false),
    onCheckedChange: (Boolean) -> Unit = {},
    onEditingFinished: (String) -> Unit = {},
    onDeleteClicked: () -> Unit = {},
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (checkbox, title, icon) = createRefs()

        val titleModifier = Modifier.constrainAs(title) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(checkbox.end)
            end.linkTo(icon.start)
            width = Dimension.fillToConstraints
        }.padding(8.dp)

        if (todo.editing)
            EditTodo(todo, titleModifier, onEditingFinished)
        else
            TodoText(todo, titleModifier)

        Checkbox(
            checked = todo.done,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.constrainAs(checkbox) {
                top.linkTo(title.top)
                bottom.linkTo(title.bottom)
                start.linkTo(parent.start)
            }
        )

        Icon(
            asset = Icons.Filled.Close,
            modifier = Modifier.clickable(onClick = onDeleteClicked)
                .constrainAs(icon) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
private fun TodoText(todo: TodoItem, modifier: Modifier) {
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
            ),
        modifier = modifier,
    )
}

@Composable
private fun EditTodo(todo: TodoItem, modifier: Modifier, onEditingFinished: (String) -> Unit) {
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
        modifier = modifier,
    )
}

private fun constraintSet(): ConstraintSet =
    ConstraintSet {
        val checkbox = createRefFor("checkbox")
        val title = createRefFor("title")
        val deleteIcon = createRefFor("deleteIcon")

        createHorizontalChain(checkbox, title, deleteIcon, chainStyle = ChainStyle.Spread)

        constrain(checkbox) {
            top.linkTo(title.top)
            bottom.linkTo(title.bottom)
            start.linkTo(parent.start)
        }
        constrain(title) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(checkbox.end)
            end.linkTo(deleteIcon.start)
        }
        constrain(deleteIcon) {
            top.linkTo(title.top)
            bottom.linkTo(title.bottom)
            end.linkTo(parent.end)
        }
    }