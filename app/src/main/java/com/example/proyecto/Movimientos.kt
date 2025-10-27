package com.example.proyecto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import kotlinx.coroutines.*

@Composable
fun CreateNewUser(onBack: () -> Unit,onHome: () -> Unit) {
    val context = LocalContext.current
    var userNameNew by remember { mutableStateOf("") }
    var userPasswordNew by remember { mutableStateOf("") }
    var userNivelNew by remember { mutableStateOf(2) }
    var loginMessage by remember { mutableStateOf("") }
    TopBarButtons(onBack = onBack, onHome = onHome)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
                iconos_contexto(iconRes = R.drawable.username, label = "New Username")
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userNameNew,
                    onValueChange = { userNameNew = it },
                    label = { Text("Nombre de usuario") }
                )
                iconos_contexto(iconRes = R.drawable.password, label = "Password")
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userPasswordNew,
                    onValueChange = { userPasswordNew = it },
                    label = { Text("Contraseña") }
                )
                DropdownSelector(
                    label = "Nivel de permiso",
                    options = listOf("Administrador", "Normal", "Consultas",),
                    selectedIndex = userNivelNew,
                    onOptionSelected = { index ->
                        userNivelNew = index
                    }
                )
                Button(
                    onClick = {
                        crearNuevoUsuario(userNameNew, userPasswordNew,
                            userNivelNew, context)
                        { mensaje -> loginMessage = mensaje }
                    }
                ) {
                    Text("Crear")
                }
                if (loginMessage.isNotEmpty()) {
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
@Composable
fun DeleteUser(onBack: () -> Unit, onHome: () -> Unit) {
    val context = LocalContext.current
    var userToDelete by remember { mutableStateOf("") }
    var loginMessage by remember { mutableStateOf("") }
    val listaUsuarios = remember { mutableStateListOf<String>() }
    val db = getDatabase(context)
    val userDao = db.userDao()
    TopBarButtons(onBack = onBack, onHome = onHome)

    // Cargar usuarios desde la DB
    LaunchedEffect(Unit) {
        val db = getDatabase(context)
        val users = db.userDao().GetAllusers()
        listaUsuarios.clear()
        listaUsuarios.addAll(users)
    }

    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ,contentAlignment = Alignment.Center) {
        Column {
            DropdownSelector(
                label = "Listado de Usuarios",
                options = listaUsuarios,
                selectedIndex = 0,
                onOptionSelected = { index ->
                    userToDelete = listaUsuarios[index]
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Eliminar usuario en background
                val db = getDatabase(context)
                val userDao = db.userDao()
                CoroutineScope(Dispatchers.IO).launch {
                    val user = userDao.getUserByUsuario(userToDelete)
                    val mensaje = if (user != null) {
                        userDao.DeletetUser(user)
                        "Usuario $userToDelete eliminado correctamente."
                    } else {
                        "El usuario $userToDelete no existe."
                    }
                    withContext(Dispatchers.Main) {
                        loginMessage = mensaje
                        // Actualizar lista
                        val updated = userDao.GetAllusers()
                        listaUsuarios.clear()
                        listaUsuarios.addAll(updated)
                    }
                }
            }) {
                Text("Eliminar Usuario")
            }

            if (loginMessage.isNotEmpty()) {
                Text(
                    text = loginMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaAMovimientosPreview() {
    //CreateNewUser(onBack = {}, onHome = {})
    DeleteUser(onBack = {}, onHome = {})
}