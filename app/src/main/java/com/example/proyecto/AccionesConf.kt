package com.example.proyecto

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource


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
                        val painter = painterResource(R.drawable.notification)
                        Image(
                            painter = painter,
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Push Notification",
                            fontSize = 20.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp).weight(1f)
                        )
                        Switch(
                            checked = pushNotificationEnabled,
                            onCheckedChange = { pushNotificationEnabled = it },
                            modifier = Modifier.padding(8.dp).graphicsLayer {
                                scaleX = 0.75f
                                scaleY = 0.75f
                            }
                        )
                    }
                }
                val opciones = listOf(
                    "Invite a friend" to R.drawable.invite,
                    "Rate this app" to R.drawable.rate,
                    "Feedback & Bugs" to R.drawable.feedback,
                    "Terms & Conditions" to R.drawable.terms,
                    "Privacy Policy" to R.drawable.privacy
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                ) {
                    opciones.forEach { (opcion, icon) ->
                        OpcionItem(
                            label = opcion,
                            iconRes = icon,
                            onClick = {
                                when (opcion) {
                                    "Invite a friend" -> println("Invitar a un amigo")
                                    "Rate this app" -> println("Calificar la app")
                                    "Feedback & Bugs" -> println("Feedback & Bugs")
                                    "Terms & Conditions" -> println("Abrir Términos y Condiciones")
                                    "Privacy Policy" -> println("Abrir Política de Privacidad")
                                }
                            }
                        )
                    }
                }
            }
        }
        LogoUan(modifier = Modifier.size(240.dp).align(Alignment.BottomCenter)
            .padding(16.dp))
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaAccionesPreview() {
    AccionesConf(onBack = {}, onHome = {})
}