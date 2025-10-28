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
import com.example.proyecto.LogoUan
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

@Composable
fun Elementos(onBack: () -> Unit, onHome: () -> Unit,onNavigateTo: (String) -> Unit){
    TopBarButtons(onBack = onBack, onHome = onHome)
    var isSelected by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // espacio entre título y card
        ) {
            Text(
                text = "Creación Elementos",
                fontSize = 32.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        Card(
            modifier = Modifier.padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(8.dp)
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                val opciones = listOf(
                    "Crear Grupo",
                    "Crear Subgrupo",
                    "Crear Elementos"
                )
                opciones.forEach { opcion ->
                    TextButton(
                        onClick = {
                            when (opcion) {
                                "Crear Grupo" -> onNavigateTo("CreateGrupo")
                                "Crear Subgrupo" -> { onNavigateTo("CreateSubgrupo") }
                                "Crear Elementos" -> { /* navegar a Elementos */ }
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
        LogoUan(
            modifier = Modifier.size(240.dp).align(Alignment.BottomCenter).padding(16.dp))
    }
}

@Composable
fun CreateGrupo(onBack: () -> Unit, onHome: () -> Unit){
    var GrupoNametocreate by remember { mutableStateOf("") }
    var GrupoCodigoNumtocreate by remember { mutableStateOf("") }
    var GrupoCodigotocreate by remember { mutableStateOf("") }
    var Message by remember { mutableStateOf("") }
    TopBarButtons(onBack = onBack, onHome = onHome)
    Box(
        modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
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
                Text(
                    text = "Nombre Grupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = GrupoNametocreate,
                    onValueChange = { GrupoNametocreate = it },
                    label = { Text("Nombre Grupo a crear") }
                )
                Text(
                    text = "Codigo Numerico del grupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp)
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = GrupoCodigoNumtocreate,
                    onValueChange = { GrupoCodigoNumtocreate = it },
                    label = { Text("Codigo Numerico del grupo a crear") }
                )
                Text(
                    text = "Codigo Grupo",
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = GrupoCodigotocreate,
                    onValueChange = { GrupoCodigotocreate = it },
                    label = { Text("Codigo Grupo a crear") }
                )
                Button(
                    onClick = {
                    }
                ){
                    Text("Crear")
                }
                if (Message.isNotEmpty()) {
                    Text(
                        text = Message,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun CreateSubgrupo (onBack: () -> Unit, onHome: () -> Unit){
    var IDGrupo by remember { mutableStateOf("") }
    var SubGrupoNametocreate by remember { mutableStateOf("") }
    var SubGrupoCodigoNumtocreate by remember { mutableStateOf("") }
    var SubGrupoCodigotocreate by remember { mutableStateOf("") }
    var Message by remember { mutableStateOf("") }
    TopBarButtons(onBack = onBack, onHome = onHome)
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)
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
                    Text(
                        text = "Grupo",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                    //lista plegable de grupo
                    Text(
                        text = "Nombre SubGrupo",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = SubGrupoNametocreate,
                        onValueChange = { SubGrupoNametocreate = it },
                        label = { Text("Nombre SubGrupo a crear") }
                    )
                    Text(
                        text = "Codigo Numerico del Subgrupo",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp)
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = SubGrupoCodigoNumtocreate,
                        onValueChange = { SubGrupoCodigoNumtocreate = it },
                        label = { Text("Codigo Num del Subgrupo a crear") }
                    )
                    Text(
                        text = "Codigo SubGrupo",
                        color = Color.Black,
                        modifier = Modifier.padding(8.dp),
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = SubGrupoCodigotocreate,
                        onValueChange = { SubGrupoCodigotocreate = it },
                        label = { Text("Codigo SubGrupo a crear") }
                    )
                    Button(
                        onClick = {
                        }
                    ){
                        Text("Crear")
                    }
                    if (Message.isNotEmpty()) {
                        Text(
                            text = Message,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
        LogoUan(modifier = Modifier.size(240.dp).padding(bottom = 16.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaAMovimientosPreview() {
    //CreateNewUser(onBack = {}, onHome = {})
    //DeleteUser(onBack = {}, onHome = {})
    //CreateGrupo(onBack = {}, onHome = {})
    //Elementos(onBack = {}, onHome = {},onNavigateTo = {})
    CreateSubgrupo (onBack = {}, onHome = {})
}
