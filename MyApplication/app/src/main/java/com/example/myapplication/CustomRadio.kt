package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadio(selected: Boolean, onClick: () -> Unit) {
    Box(modifier = Modifier.clickable {
        onClick()
    }) {
        if(selected) RadioSelected()
        else RadioUnSelected()
    }
}

@Composable
private fun RadioSelected() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun RadioUnSelected() {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(10.dp)
            ),
    )
}

@Composable
fun VerticalRadio(radioState: MutableList<RadioState>) {
    Column {
        radioState.forEachIndexed { index, state ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    radioState.replaceAll {
                        it.copy(selected = it.s == state.s)
                    }
                }.padding(end = 16.dp)
            ) {
                CustomRadio(
                    selected = state.selected,
                    onClick = {
                        radioState.replaceAll {
                            it.copy(selected = it.s == state.s)
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun HorizontalRadio(radioState: MutableList<RadioState>) {
    Row {
        radioState.forEachIndexed { index, state ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable {
                    radioState.replaceAll {
                        it.copy(selected = it.s == state.s)
                    }
                }.padding(end = 16.dp)
            ) {
                CustomRadio(
                    selected = state.selected,
                    onClick = {
                        radioState.replaceAll {
                            it.copy(selected = it.s == state.s)
                        }
                    }
                )
            }
        }
    }

}

@Preview
@Composable
private fun RadioButtonPreview() {
    val radioState = remember {
        mutableStateListOf(
            RadioState(true, "Option 1"),
            RadioState(false, "Option 2"),
            RadioState(false, "Option 3")
        )
    }
    HorizontalRadio(radioState)
}

data class RadioState(
    val selected: Boolean,
    val s: String
)