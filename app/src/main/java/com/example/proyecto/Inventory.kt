package com.example.proyecto

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement

@Composable
fun Inventory(onBack:() -> Unit,onHome: () -> Unit){
    TopBarButtons(onBack = onBack, onHome = onHome)

    Box(modifier = Modifier.fillMaxSize()){

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaInventoryPreview() {
    Inventory(onBack = {}, onHome = {})
}