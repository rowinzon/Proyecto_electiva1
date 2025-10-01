package com.example.proyecto
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
@Composable
fun PantallaPrincipal(onLoginSuccess: () -> Unit,onBack: () -> Unit) {
    var userName by remember { mutableStateOf("") }
    var passwordUser by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        TextButton(onClick = { onBack() }) {
            Text(
                text = "volver",
                color = Color.Black,
                fontSize = 18.sp
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Título de la app
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "INVENTORY-APP",
                fontSize = 30.sp,
                color = Color.Black
            )
        }
        //imagen uan

        // Card con formulario
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Username
                iconos_contexto(iconRes = R.drawable.username, label = "Username")
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text("Nombre de usuario") }
                )
                // Password
                iconos_contexto(iconRes = R.drawable.password, label = "Password")
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordUser,
                    onValueChange = { passwordUser = it },
                    label = { Text("Contraseña") }
                )
                // Recordarme + Forget password
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
                    TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                    onClick = { loginMessage ="funciona el boton" }
                    ) {
                    Text(
                        text = "Forget Password",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
                }
                // Botón Login
                Button(
                    onClick = {
                        if (userName == "Prueba") {
                            onLoginSuccess()
                        } else {
                            loginMessage = "Usuario incorrecto"
                        }
                    }
                ) {
                    Text("Login")
                }
                // Mensaje de error
                if (loginMessage.isNotEmpty()) {
                    Text(
                        text = loginMessage,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        LogoUan(modifier = Modifier.size(240.dp).align(Alignment.BottomCenter)
            .padding(16.dp))
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaPrimerPPreview() {
    PantallaPrincipal(onLoginSuccess = {}, onBack = {})
}