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
import androidx.compose.ui.graphics.graphicsLayer


@Composable
fun AccionesConf(onBack: () -> Unit,onHome: () -> Unit) {
    TopBarButtons(onBack = onBack, onHome = onHome)

    var pushNotificationEnabled by remember { mutableStateOf(false) }

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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Push Notification",
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = pushNotificationEnabled,
                            onCheckedChange = { pushNotificationEnabled = it },
                            modifier = Modifier.graphicsLayer {
                                scaleX = 0.75f
                                scaleY = 0.75f
                            }
                        )
                    }
                }
                val opciones = listOf(
                    "Invite a friend",
                    "Rate this app",
                    "Feedback & Bugs",
                    "Terms & Conditions",
                    "Privacy Policy"
                )

                opciones.forEach { opcion ->
                    TextButton(
                        onClick = {
                            when (opcion) {
                                "Invite a friend" -> {
                                    // Acción para invitar a un amigo
                                    println("Invitar a un amigo")
                                }
                                "Rate this app" -> {
                                    // Acción para calificar la app
                                    println("Calificar la app")
                                }
                                "Feedback & Bugs" -> {
                                    // Acción para enviar feedback
                                    println("Feedback & Bugs")
                                }
                                "Terms & Conditions" -> {
                                    // Acción para abrir Términos y Condiciones
                                    println("Abrir Términos y Condiciones")
                                }
                                "Privacy Policy" -> {
                                    // Acción para abrir la Política de Privacidad
                                    println("Abrir Política de Privacidad")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = opcion,
                            fontSize = 20.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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