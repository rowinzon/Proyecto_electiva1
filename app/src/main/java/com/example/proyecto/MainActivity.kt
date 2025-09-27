package com.example.proyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.proyecto.ui.theme.ProyectoTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoTheme {
                var pantallaActual by remember { mutableStateOf("login") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (pantallaActual) {
                        "login" -> Greeting(
                            modifier = Modifier.padding(innerPadding),
                            onLoginSuccess = { pantallaActual = "acciones" }
                        )
                        "acciones" -> AccionesConf()
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(modifier: Modifier = Modifier, onLoginSuccess: () -> Unit) {
    var userName by remember { mutableStateOf("") }
    var passwordUser by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf("") }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp), // ajusta la distancia desde arriba
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "INVENTORY-APP",
                fontSize = 30.sp,
                color = Color.Black
            )
        }
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Username", fontSize = 20.sp, color = Color.Black)

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Nombre de usuario") }
                )

                Text("Password", fontSize = 20.sp, color = Color.Black)

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordUser,
                    onValueChange = { passwordUser = it },
                    label = { Text("Contraseña") }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        )
                        Text("Recordarme", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Forget password", color = Color.Black)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (userName == "Prueba") {
                            onLoginSuccess()
                        } else {
                            loginMessage = "usuario incorrecto"
                        }
                    }
                ) {
                    Text("Login")
                }

                if (loginMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = loginMessage,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoTheme {
        Greeting(onLoginSuccess = {})
    }
}