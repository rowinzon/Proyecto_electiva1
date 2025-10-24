package com.example.proyecto

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyecto.ui.theme.ProyectoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crearUsuarioAdministrador(this)
        enableEdgeToEdge()
        setContent {
            ProyectoTheme {
                var pantallaActual by remember { mutableStateOf("login") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (pantallaActual) {
                        // HOME
                        "login" -> Greeting(
                            modifier = Modifier.padding(innerPadding),
                            onNavigate = { destino ->
                                pantallaActual = when (destino) {
                                    "Log In" -> "principal"
                                    "Settings" -> "settings"
                                    "Inventory" -> "inventory"
                                    else -> pantallaActual
                                }
                            }
                        )

                        // LOGIN / PRINCIPAL
                        "principal" -> PantallaPrincipal(
                            onLoginSuccess = { pantallaActual = "inventory" }, // va a la pantalla de inventario
                            onBack = { pantallaActual = "login" }
                        )
                        // SETTINGS
                        "settings" -> AccionesConf(
                            onBack = { pantallaActual = "login" }, // regresa a principal
                            onHome = { pantallaActual = "login" }      // vuelve al home
                        )
                        //Inventory
                        "inventory" -> Inventory(
                            onBack = { pantallaActual = "principal" }, // vuelve a login si quieres
                            onHome = { pantallaActual = "login" }      // vuelve al home
                        )
                    }
                }
            }
        }
    }
}

// ---------- HOME ----------
@Composable
fun Greeting(modifier: Modifier = Modifier, onNavigate: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = modifier.padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("INVENTORI-APP", fontSize = 30.sp, color = Color.Black)
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val opciones = listOf("Log In", "Settings")
                    opciones.forEach { opcion ->
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            onClick = { onNavigate(opcion) }
                        ) {
                            Text(
                                text = opcion,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
            LogoUan(modifier = Modifier.size(240.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoTheme {
        Greeting(onNavigate = {})
    }
}
