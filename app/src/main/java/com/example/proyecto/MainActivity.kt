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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.proyecto.ui.theme.ProyectoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crearUsuarioAdministrador(this)
        enableEdgeToEdge()

        setContent {
            ProyectoTheme {
                var pantallaActual by remember { mutableStateOf("login") }
                var usuarioActual by remember { mutableStateOf<User?>(null) }

                Scaffold(modifier = Modifier) { innerPadding ->
                    when (pantallaActual) {
                        "login" -> Greeting(
                            modifier = Modifier.padding(innerPadding),
                            onLoginSuccess = { user ->
                                usuarioActual = user
                                pantallaActual = "inventory"
                            }
                        )
// ---------- HOME ----------
                        "inventory" -> Inventory(
                            UsuarioLogeado = usuarioActual?.usuario ?: "",
                            Nivelacceso = usuarioActual?.nivelDePermiso ?: 2,
                            onBack = { pantallaActual = "login" },
                            onHome = { pantallaActual = "inventory" },
                            onNavigateTo = { destino -> pantallaActual = destino }
                        )
// ---------- PAGINA SETTINGS ----------
                        "settings" -> AccionesConf(
                            onBack = { pantallaActual = "inventory" },
                            onHome = { pantallaActual = "inventory" }
                        )
                    }
                }
            }
        }
    }
}
// ---------- PAGINA INICIAL ----------
@Composable
fun Greeting(modifier: Modifier = Modifier, onLoginSuccess: (User) -> Unit) {
    val context = LocalContext.current
    var userNameingresado by remember { mutableStateOf("") }
    var passwordUseringresado by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var loginMessage by remember { mutableStateOf("") }

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
                    value = userNameingresado,
                    onValueChange = { userNameingresado = it },
                    label = { Text("Nombre de usuario") }
                )
                // Password
                iconos_contexto(iconRes = R.drawable.password, label = "Password")
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordUseringresado,
                    onValueChange = { passwordUseringresado = it },
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
                // Botón
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val db = getDatabase(context)
                            val userDao = db.userDao()

                            // Buscar el usuario por su nombre
                            val usuarioEncontrado = userDao.getUserByUsuario(userNameingresado)

                            withContext(Dispatchers.Main) {
                                if (usuarioEncontrado != null) {
                                    // Validar usuario y contraseña
                                    if (usuarioEncontrado.password == passwordUseringresado) {
                                        loginMessage = "Inicio de sesión exitoso"
                                        onLoginSuccess(usuarioEncontrado)
                                    } else {
                                        loginMessage = "Contraseña o usuario incorrectos"
                                    }
                                } else {
                                    loginMessage = "Usuario no encontrado"
                                }
                            }
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
fun GreetingPreview() {
    ProyectoTheme {
        Greeting(onLoginSuccess = {})
    }
}
