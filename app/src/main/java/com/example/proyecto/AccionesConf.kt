package com.example.proyecto

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign


@Composable
fun AccionesConf(onBack: () -> Unit,onHome: () -> Unit) {
    TopBarButtons(onBack = onBack, onHome = onHome)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        var isSelected by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Settings", fontSize = 30.sp, color = Color.Black)
        }
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Push Notification",
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = { isSelected = !isSelected },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color.Blue else Color.Gray
                        ),
                        modifier = Modifier
                            .width(40.dp)
                            .height(30.dp)
                    ) {}
                }
                val opciones = listOf(
                    "Invite a friend",
                    "Rate this app",
                    "Feedback & Bugs",
                    "Terms & Conditions",
                    "Privacy Policy"
                )
                opciones.forEach { opcion ->
                    Text(
                        text = opcion,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }

    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaAccionesPreview() {
    AccionesConf(onBack = {}, onHome = {})
}