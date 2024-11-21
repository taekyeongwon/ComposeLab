package com.example.myapplication

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CheckBox(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    var checkBoxState by remember { mutableStateOf(checked) }

    Card(
        modifier = modifier
            .size(24.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = {
            checkBoxState = !checkBoxState
            onCheckedChange(checkBoxState)
        },
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        if (checkBoxState) CheckBoxSelected()
        else CheckBoxUnSelected()
    }
}

@Composable
private fun CheckBoxSelected() {
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
private fun CheckBoxUnSelected() {
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

@Preview
@Composable
private fun CheckBoxPreview() {
    CheckBox(checked = false, onCheckedChange = {})

}